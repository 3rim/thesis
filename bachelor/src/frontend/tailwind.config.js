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
        "primary": "#00668A"
      }
    },
    container:{
      padding:"2em",
      center:true,
    }, 
  },
  plugins: [],
}

