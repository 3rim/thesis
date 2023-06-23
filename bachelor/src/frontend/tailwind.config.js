const { Container } = require('postcss');

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors:{
        "primary": "#ecf0f1",
        "navbar": "#f7f3e3",
        "navbar-buttons" :"#e4d4ba"
      }
    },
    container:{
      padding:"2em",
      center:true,
    }, 
  },
  plugins: [],
}

