import javax.swing.*;
import java.awt.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;

public class reCAPTCHA extends JFrame {
    
    public reCAPTCHA(SignIn mainFrame) {
        // JFrame 설정
        setTitle("리캡챠 인증");
        setSize(800, 600);

        // JFXPanel을 사용하여 JavaFX의 WebView를 Swing에 추가
        JFXPanel jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.CENTER);
        
        
        // JavaFX 애플리케이션에서 WebView 설정
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                WebView webView = new WebView();
                WebEngine webEngine = webView.getEngine();

                // JSP 페이지 URL (Tomcat 서버에서 실행 중인 페이지)
                String recaptchaUrl = "http://localhost:8181/Re/index.jsp"; // JSP 페이지 URL
                webEngine.load(recaptchaUrl);

                // 인증 결과를 받기 위한 alert 이벤트 리스너 설정
                webEngine.setOnAlert(event -> {
                    String result = event.getData(); // JavaScript에서 전달된 인증 결과
                    if ("true".equals(result)) {
                        System.out.println("리캡챠 인증 성공!");
                        mainFrame.updateResult(true); // 인증 성공 결과를 MainFrame에 전달
                    } else {
                        System.out.println("리캡챠 인증 실패!");
                        mainFrame.updateResult(false); // 인증 실패 결과를 MainFrame에 전달
                    }
                    setVisible(false);  // 인증 후 창을 닫기
                });

                // Scene을 생성하고 WebView를 추가
                Scene scene = new Scene(webView, 800, 600);
                jfxPanel.setScene(scene);  // JFXPanel에 JavaFX Scene 설정
            }
        });

        setVisible(true);
    }
}



