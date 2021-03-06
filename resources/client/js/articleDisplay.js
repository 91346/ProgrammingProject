"use strict";

//articleGet() returns one row of data from the database using a GET and path parameter
function articleGet() {
    debugger;
    console.log("Invoked articleGet()");     //console.log for debugging client side
    const ArticleID = document.getElementById("ArticleID").value;  //get the ArticleId from the HTML element with id=ArticleID
    //let ArticleID = 1; 			  //for hard code it if problems
    debugger;				          //debugger statement to allow stepping through the code in console dev F12
    const url = "/article/get/";      // API method on webserver
    fetch(url + ArticleID, {                // UserID as a path parameter
        method: "GET",
    }).then(response => {
        return response.json();                         //return response to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {         //checks if response from server has an "Error"
            alert(JSON.stringify(response));            // if it does, convert JSON object to string and alert
        } else {
            document.getElementById("DisplayArticle").innerHTML = " " + response.Title + "<br>" + response.Description + "<br>Topic: " + response.Topic + "<br>Author: " + response.Author + "<br>Date Created: " + response.Date + "<br>Picture: " + response.Picture + "<br>" + response.Article;  //output data
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
