document.addEventListener('DOMContentLoaded', function() {
    let sidebar = document.querySelector(".sidebar");
    let closeBtn = document.querySelector("#btn");
    let searchBtn = document.querySelector(".bx-search");

    if (closeBtn && searchBtn) {
        closeBtn.addEventListener("click", () => {
            sidebar.classList.toggle("open");
            menuBtnChange(); //calling the function(optional)
        });

        searchBtn.addEventListener("click", () => {
            sidebar.classList.toggle("open");
            menuBtnChange(); //calling the function(optional)
        });
    }

    // Function to change sidebar button icons
    function menuBtnChange() {
        if (sidebar.classList.contains("open")) {
            closeBtn.classList.replace("bx-menu", "bx-menu-alt-right");
            closeBtn.classList.replace("bx-chevron-right", "bx-chevron-left");
        } else {
            closeBtn.classList.replace("bx-menu-alt-right", "bx-menu"); 
            closeBtn.classList.replace("bx-chevron-left", "bx-chevron-right");
        }
    }
});