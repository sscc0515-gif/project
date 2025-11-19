const express = require('express');
const app = express();
const path = require('path');

app.set('view engine', 'pug');
app.set('views', path.join(__dirname, 'views'));

app.use('/images', express.static(path.join(__dirname, 'images')));

app.get('/', (req, res) => {
  res.render('index');
});

app.listen(4000, () => console.log('Server running on http://localhost:4000'));
