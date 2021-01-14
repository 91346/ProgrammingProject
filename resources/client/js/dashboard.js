"use strict";
//UNFINISHED
//getUserDetails function to get the
function getBookmarks() {
    console.log("Invoked getBookmarks()");
    debugger;
    let token = Cookies.get('Token'); //gets the token from the current user
    const formData = new FormData();
    formData.append('Token', token);
    const url = "/user/getBookmarks/";    // API method on web server will be in Users class, method list
    fetch(url + formData,{
        method: "GET",		//Get method
    }).then(response => {
        return response.json();      //return response as JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) { //checks if response from the web server has an "Error"
            alert(JSON.stringify(response));    // if it does, convert JSON object to string and alert (pop up window)
        } else {
            /*/let dataHTML = "";
            for (let item of myJSONArray) {
                dataHTML += "<tr><td>" + item.____ + "<td><td>" + item._____ + "<tr><td>";
            }*/
            document.getElementById("Bookmarks").innerHTML = response;          //this function will create an HTML table of the data (as per previous lesson)
        }
    });
}
