"use strict";
//UNFINISHED
//getUserDetails function to get the
function getBookmarks() {
    console.log("Invoked getBookmarks()");
    debugger;
    let token = Cookies.get('Token'); //gets the token from the current user
    const Token = token;
    const url = "/user/getBookmarks/";    // API method on web server will be in Users class, method list
    fetch(url + Token,{
        method: "GET",		//Get method
    }).then(response => {
        return response.json();      //return response as JSON
        console.log("returned bookmarks");
    }).then(response => {
        if (response.hasOwnProperty("Error")) { //checks if response from the web server has an "Error"
            alert(JSON.stringify(response));    // if it does, convert JSON object to string and alert (pop up window)
        } else {
            console.log("else bookmarks");
            document.getElementById("Bookmarks").innerHTML = response.Title + "<tr><td>" + response.Description + "<tr><td>" + response.Topic + "  " + response.Author + "  " + response.Date + "<tr><td>" + response.Picture;          //this function will create an HTML table of the data
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
