import withMT from "@material-tailwind/html/utils/withMT.js";

/** @type {import('tailwindcss').Config} */
export default withMT({
    content: [
        "./index.html",
        "./src/components/*.{vue,js,ts,jsx,tsx}",
        "./src/layout/*.{vue,js,ts,jsx,tsx}",
        "./src/views/*.{vue,js,ts,jsx,tsx}",
    ],
    theme: {
        extend: {},
    },
    plugins: [],
})

