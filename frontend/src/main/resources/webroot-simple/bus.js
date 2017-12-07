var eb = new EventBus('/simple/eventbus');
eb.onopen = function() {
    var d1 = document.getElementById('one');
    eb.registerHandler("example-browser", function (error, message) {
        console.log("received " + JSON.stringify(message));
        d1.insertAdjacentHTML('beforeend', '<div id="two">'+JSON.stringify(message)+'</div>');
    });
    eb.send("example-server", "from browser");
};
