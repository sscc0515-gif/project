package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private FirebaseAuth auth;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LatLng objLocation;
    private Marker lastClickedMarker = null;
    private DatabaseReference databaseMarkers;
    private final List<Marker> savedMarkers = new ArrayList<>();
    private final Map<Marker, String> markerIdMap = new HashMap<>(); // 마커 ↔ Firebase 키 연결



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        databaseMarkers = FirebaseDatabase.getInstance().getReference("markers");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        binding.btnLogout.setOnClickListener(v -> {
            auth.signOut();   // 세션 삭제

            // 로그인 화면으로 이동 & 백스택 비우기
            Intent i = new Intent(this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();         // MapsActivity 종료
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        LatLng schoolLocation = new LatLng(37.450520, 126.657663);
        objLocation = schoolLocation;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(schoolLocation, 15f));
        mMap.addMarker(new MarkerOptions().position(schoolLocation).title("인하공업전문대학"));

        // 위치 권한 확인
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    100);
            return;
        }

        mMap.setMyLocationEnabled(true);

        // 🔥 음식점 자동 로딩
        Location fakeLocation = new Location("fixed");
        fakeLocation.setLatitude(schoolLocation.latitude);
        fakeLocation.setLongitude(schoolLocation.longitude);
        loadNearbyRestaurants(fakeLocation);

        // 사용자가 지도를 클릭하면 마커 추가
        mMap.setOnMapClickListener(latLng -> {
            // 사용자에게 마커 제목 입력을 요청하는 다이얼로그
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("마커 제목 입력");

            final EditText input = new EditText(this);
            input.setHint("예: 내 즐겨찾기 장소");
            builder.setView(input);

            builder.setPositiveButton("확인", (dialog, which) -> {
                String title = input.getText().toString().trim();
                if (title.isEmpty()) {
                    title = "저장된 위치"; // 기본 제목
                }

                Marker newMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title));

                savedMarkers.add(newMarker);
                Toast.makeText(this, "마커 추가됨", Toast.LENGTH_SHORT).show();

                String markerId = databaseMarkers.push().getKey();
                if (markerId != null) {
                    MarkerData markerData = new MarkerData(markerId, title, latLng.latitude, latLng.longitude);
                    databaseMarkers.child(markerId).setValue(markerData);

                    newMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(title));
                    savedMarkers.add(newMarker);
                    markerIdMap.put(newMarker, markerId); // Firebase 키 저장
                }

            });

            builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

            builder.show();
        });


        mMap.setOnMarkerClickListener(marker -> {
            // 마커를 연속으로 두 번 클릭하면 삭제 다이얼로그 표시
            if (marker.equals(lastClickedMarker)) {
                new AlertDialog.Builder(this)
                        .setTitle("마커 삭제")
                        .setMessage("이 마커를 삭제하시겠습니까?")
                        .setPositiveButton("삭제", (dialog, which) -> {
                            String markerId = markerIdMap.get(marker);
                            if (markerId != null) {
                                databaseMarkers.child(markerId).removeValue(); // Firebase에서 삭제
                            }

                            marker.remove(); // 지도에서 삭제
                            savedMarkers.remove(marker);
                            markerIdMap.remove(marker);
                            lastClickedMarker = null;

                            Toast.makeText(this, "마커 삭제됨", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("취소", (dialog, which) -> lastClickedMarker = null)
                        .show();
            } else {
                // 첫 번째 클릭: 이름만 보여줌
                marker.showInfoWindow(); // 마커 이름 툴팁 띄우기
                lastClickedMarker = marker;
            }

            return true; // 기본 클릭 동작(지도 이동 등)은 막음
        });

        loadSavedMarkersFromFirebase();
    }


    // ✅ 음식점 마커 불러오기
    private void loadNearbyRestaurants(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        int radius = 1000; // 1km 반경
        String type = "restaurant";
        String apiKey = "AIzaSyATeAyx500dxLNxJry-hNgCVUaIX8af41c"; // 여기에 본인의 Google API 키 입력

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + latitude + "," + longitude +
                "&radius=" + radius +
                "&type=" + type +
                "&key=" + apiKey;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject place = results.getJSONObject(i);
                            String name = place.getString("name");
                            JSONObject locationObj = place.getJSONObject("geometry").getJSONObject("location");
                            double lat = locationObj.getDouble("lat");
                            double lng = locationObj.getDouble("lng");

                            LatLng latLng = new LatLng(lat, lng);
                            mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "파싱 오류: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "요청 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }

    private void loadSavedMarkersFromFirebase() {
        databaseMarkers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MarkerData markerData = snapshot.getValue(MarkerData.class);
                    if (markerData != null) {
                        LatLng position = new LatLng(markerData.latitude, markerData.longitude);
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(position)
                                .title(markerData.name));

                        savedMarkers.add(marker);
                        markerIdMap.put(marker, markerData.id); // ✅ Firebase 키를 마커에 연결
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MapsActivity.this, "Firebase 로딩 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

