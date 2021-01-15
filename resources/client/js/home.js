"use strict";
function logout() {
    debugger;
    console.log("Invoked logout");
    let url = "/user/logout";
    fetch(url, {
        method: "POST",
    }).then(response => {
        return response.json();                 //now return that promise to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));        // if it does, convert JSON object to string and alert
        } else {
            Cookies.remove("Token", response.Token);    //Username and Token are removed
            Cookies.remove("Username", response.Username);
            Cookies.remove("Admin", response.Admin);
            window.open("home.html", "_self");       //open home.html in same tab
        }
    });
}
