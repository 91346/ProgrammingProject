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
