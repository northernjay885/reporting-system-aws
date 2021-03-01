function loadAll() {
    $("#report_list_body").html("");

    $.getJSON('/report',
        function (data, textStatus, jqXHR) {  // success callback
            console.info(data);
            data.data.forEach((report, index)=>{
                $("#report_list_body").append(
                    $('<tr>').append(
                        $('<td>').append(index + 1)
                    ).append(
                        $('<td>').append(report.submitter)
                    ).append(
                        $('<td>').append(report.description)
                    ).append(
                        $('<td>').append(formatTime(report.createdTime))
                    ).append(
                        $('<td>').append(report.pdfReportStatus)
                    ).append(
                        $('<td>').append(report.excelReportStatus)
                    ).append(
                        "<td>" + actionList(report.pdfReportStatus, report.excelReportStatus, report.id) + "</td>"
                    )
                );
            });

        },function(e){
            alert('error' + e.error);
        }
    );
}
function formatTime(time) {
    if(!time){
        return "N/A";
    }
    const d = new Date(time);
    return singleDigit(d.getMonth() + 1) + '/'+singleDigit(d.getDate()) + ' ' + singleDigit(d.getHours()) + ':' + singleDigit(d.getMinutes());
}
function singleDigit(dig) {
    return ('0' + dig).slice(-2)
}
function downloadPDF(reqId){
    downloadFile('/report/content/' + reqId + '/PDF');
}
function downloadExcel(reqId){
    downloadFile('/report/content/'+ reqId + '/EXCEL');
}
function downloadFile(urlToSend) {
    var req = new XMLHttpRequest();
    req.open("GET", urlToSend, true);
    req.responseType = "blob";
    req.onload = function (event) {
        console.info(event);
        if(req.status === 200) {
            var blob = req.response;
            var fileName = req.getResponseHeader("fileName")
            var link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = fileName;
            link.click();
        } else{
            alert('Error in downloading')
        }
    };
    req.send();
}
function showDelete(reqId){
    if(confirm("Are you sure to delete report?")){
        $.ajax({
            type : "DELETE",
            url : "/report/content/"+ reqId,
            success: function (result) {
                console.log(result);
                window.location = '/'; // redirect
            },
            error: function (e) {
                console.log(e);
            }
        });
    }
}

function myFunction() {
    console.log("I'm in myFunction");
    document.getElementById("myDropdown").classList.toggle("show");
}

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
    if (!event.target.matches('.dropbtn')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}

function actionList(ps, es, id) {
    return "<button onclick='myFunction()' class='dropbtn'>Actions</button>" +
            "<div id='myDropdown' class='dropdown-content'>" +
                actionLinks(ps, es, id) +
            "</div>";
}

function actionLinks(ps, es, id) {
    return (ps === 'COMPLETED'?"<a onclick='downloadPDF(\""+id+"\")' href='#'>Download PDF</a>":"")
         + (es === 'COMPLETED'?"<a onclick='downloadExcel(\""+id+"\")' href='#'>Download Excel</a>":"")
         + "<a onclick='showDelete(\""+id+"\")' href='#'>Delete</a>";
}
function validateInput(){
    try {
        return JSON.parse($('#inputData').val());
    }catch(err) {
        alert("This is not a valid Json.");
        return "";
    }
}

function submit(async) {
    let data = validateInput();
    if(!data) {
        return false;
    }
    $.ajax({
        url : async?"report/async":"report/sync",
        type: "POST",
        data : JSON.stringify(data),
        contentType: "application/json",
        dataType: "json",
        success: function(data, textStatus, jqXHR)
        {
            console.info(data);
            $('#create_report_model').modal('toggle');
            loadAll();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(jqXHR.responseJSON.message);
            console.error(jqXHR);
            console.error(jqXHR.responseJSON.message);
        }
    });
}
$( document ).ready(function() {
    loadAll();
    $("#loadAllBtn").on("click",function () {
        loadAll();
    });
    $("#generateBtn").on("click",function () {
        $('#create_report_model').modal('toggle');
    });
    $("#create_report").on("click",function () {
        submit(false);
    });
    $("#create_report_async").on("click",function () {
        submit(true);
    });
});
