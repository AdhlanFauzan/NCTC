/**
 * Created by Ubuntu on 20.05.2015.
 */
$(document).ready(function () {
    getUrlVars();
    getOrderById(getUrlVars()["publicToken"]);
    ymaps.ready(init);
});

function getOrderById(publicToken) {
    $.ajax({
        method: 'GET',
        url: "api/order/viewOrderByPublicToken?publicToken=" + publicToken,
        dataType: 'text',
        success: function (data) {
            var obj = JSON.parse(data);
            $("#contactName").attr("value", obj.contactName);
            $("#contactPhone").attr("value", obj.contactPhone);
            $("#requestedSeatsCount").attr("value", obj.requestedSeatsCount);
            $("#fromAddress").attr("value", obj.fromAddress);
            $("#fromX").attr("value", obj.fromX);
            $("#fromY").attr("value", obj.fromY);

            //TODO add dynamic generation
            $("#toAddress0").attr("value", obj.toAddress[0]);
            $("#toX0").attr("value", obj.toX[0]);
            $("#toY0").attr("value", obj.toY[0]);
            $("#distance0").attr("value", obj.distance[0]);

            $("#timeRequested").attr("value", obj.timeRequested);

            $('input:radio[name=sex][data-value=' + obj.sex + ']').prop('checked', true);

            $("#car").attr("value", obj.carClass);
            $("#music").attr("value", obj.musicType);

            if (obj.smokingFriendly == "true") {
                $('input:checkbox[id=smokingFriendly]').prop('checked', true);
            }
            if (obj.animalFriendly == "true") {
                $('input:checkbox[id=animalFriendly]').prop('checked', true);
            }
            if (obj.wifi == "true") {
                $('input:checkbox[id=wifi]').prop('checked', true);
            }
            if (obj.airConditioner == "true") {
                $('input:checkbox[id=airConditioner]').prop('checked', true);
            }

            //TODO this value is empty
            $("#totalMultiplier").attr("value", obj.totalMultiplier);
            $("#totalLength").attr("value", obj.totalLength);
            $("#totalPrice").attr("value", obj.totalPrice);

            $("#customerPreCreateComment").attr("value", obj.customerPreCreateComment);
        },
        error: function (jqXHR) {
            alert(jqXHR.responseText);
            document.location.href = "index.jsp";
        }
    });
}

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
        vars[key] = value;
    });
    return vars;
}