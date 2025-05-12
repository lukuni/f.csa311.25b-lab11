const express = require('express');
const app = express();
const port = 8080;

app.get('/newgame', (req, res) => {
    res.json({ message: 'Starting a new game!' });
});


app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}`);
});
