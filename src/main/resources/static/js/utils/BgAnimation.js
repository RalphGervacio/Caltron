$(document).ready(function () {
    const symbols = ["+", "−", "×", "%"];
    const colors = ["red", "green", "orange", "purple"];

    // Generate 30 random shapes across the page
    for (let i = 0; i < 30; i++) {
        const $shape = $("<span></span>")
                .addClass("math-shape " + colors[Math.floor(Math.random() * colors.length)])
                .text(symbols[Math.floor(Math.random() * symbols.length)])
                .css({
                    position: "absolute",
                    top: (Math.random() * 100) + "vh",
                    left: (Math.random() * 100) + "vw",
                    fontSize: (Math.random() * 2 + 1.5) + "rem",
                    animationDuration: (20 + Math.random() * 30) + "s",
                    pointerEvents: "none" // so it won’t block clicks
                });

        $("body").append($shape);
    }
});
