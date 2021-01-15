"use strict";
//createArticle function to add an article to the database
function createArticle() {
    console.log("Invoked CreateArticle()");
    debugger;
    let token = Cookies.get('Token'); //gets the token from the current user
    let author = Cookies.get('Username'); //gets the username of the current user
    const formData = new FormData(document.getElementById('InputArticle'));
    formData.append('Token', token);
    formData.append('Author', author);
    let url = "/article/add";
    fetch(url, {
        method: "POST",
        body: formData,
    }).then(response => {
        return response.json()
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));
        } else {
            alert(JSON.stringify("New Article Created \r If your article needs updating, please contact an Administrator")); //pops up a window showing message
            window.open("dashboard.html", "_self"); //takes user back to dashboard
        }
    });
}

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
