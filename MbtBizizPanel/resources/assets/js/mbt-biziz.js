
function addNewsClicked() {
    $.ajax({
        url: 'addnewsEmpty',
        type: "GET",
        dataType: "json",
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later. Error Code : 011")
            } else {
                // do nothing, newscontroller will forward to editnews.
            }
        },
        error: function (request, error) {
            console.log(arguments);
        }
    });
}

function addImage(imageData){

    var slideCount = splideImg? splideImg.length : 0;
    var isFirst = slideCount == 0;

    if (isFirst){
        //$('#CoverPhotoDiv').show(); //delete
        //$('#CoverPhoto').attr("src", imageData); //delete    
        $('#SliderDiv').show(); 
        $('#DeleteImageButton')[0].style.display = '';          

        if (!splideImg){
            splideImg = new Splide( '.splide' ).mount();            
        }
    }
    $("input[name='imageString"+(slideCount==0?'':slideCount)+"']").val(imageData);
    splideImg.add('<li class="splide__slide"> <img src="'+imageData+'" style="width:260px; margin-left:60px;padding-bottom:30px"></li>'); 
}

function imageInit(){
    var isImage = $("input[name='imageString']")[0].value!="";
    if (isImage){
        $('#SliderDiv').show();   
        $('#DeleteImageButton')[0].style.display = '';          
        $('#EditNewsImages').empty();        
        var imagesHtml = "";        
        for (i = 0; i<10; i++) {
            var imageStr = "imageString"+(i==0?'':i);
            var imageData = $("input[name='"+imageStr+"']")[0].value;           
            if (imageData!=""){
                imagesHtml += '<li class="splide__slide"> <img src="'+imageData+'" style="width:260px; margin-left:60px;padding-bottom:30px"></li>';
            }else{
                break;
            }
        }    
        $('#EditNewsImages').append(imagesHtml);    
        splideImg = new Splide( '.splide' ).mount();        
    }
}

function deleteImage(){
   var index = splideImg.index;
    for (i = index; i<10; i++) {
        var imageStr = "imageString"+(index==0?'':index);
        var imageStrNext = "imageString"+(index+1);        
        $("input[name="+imageStr+"]").val($("input[name="+imageStrNext+"]").value);
    }
    $("input[name='imageString9']").val("");
    
    var isLast = splideImg.length == 1;
    splideImg.remove(index);
    if (isLast){
        //$('#AddCoverPhotoDiv').show();    //delete
        //$("input[name='imageString']").val(''); //delete
        //$('#CoverPhotoDiv').hide();    
        $('#SliderDiv').hide();    
        $('#DeleteImageButton')[0].style.display = 'none';          
    } 

    //$('#CoverPhoto').attr("src", ""); //delete
    $('.cropit-preview-image').attr("src", "");
}

$(function () {
    $('.image-editor').cropit({
        allowDragNDrop: false,
        smallImage:'allow',
        exportZoom: 2
    });

    $('#savecoverphoto').click(function () {
        // Move cropped image data to hidden input
        var imageData = $('.image-editor').cropit('export');
        addImage(imageData);
    });
});

var j = 5;
var splideImg = null;

if (document.getElementById("startTime_")) {
    var today = moment().format('YYYY-MM-DDT09:00');
    document.getElementById("startTime_").value = today;
}

$("[name='deleteimage']").click(function () {
    deleteImage();
});

$("[name='deleteimageinedit']").click(function () {
    deleteImage();
});

$("#sortByDate").click(function () {
    newssayi = 1;
    j = 5;
    $("#sortBy").val(2);
    $("#allNews").empty();
    getNewsList();
    $("#sortByDate").css('text-decoration', 'underline');
    $("#sortByCreatedAt").css('text-decoration', 'none');

});

$("#sortByCreatedAt").click(function () {
    newssayi = 1;
    j = 5;
    $("#sortBy").val(1);
    $("#allNews").empty();
    getNewsList();
    $("#sortByCreatedAt").css('text-decoration', 'underline');
    $("#sortByDate").css('text-decoration', 'none');
});


//PDF BEGIN
var fileno_pdf = 0;

$("[name='upload_document']").click(function () {
    $("[name='pdf_inputfile']").append("<input type='file' id='pdf" + fileno_pdf + "' name='file_pdf[]' accept='application/pdf' onchange='pdfupload(this);' hidden>");
    $("#pdf" + fileno_pdf).click()
});

function pdfclick(url) {
    window.open(decodeURIComponent(url), "_blank");
}

function safeUrl(u) {
  if (typeof u !== 'string') return '';
  u = u.trim();

  // sadece http/https veya relative path kabul et
  if (/^https?:\/\/[^\s"'<>]+$/i.test(u)) return u;
  if (/^\/[^\s"'<>]*$/i.test(u)) return u;

  return '';
}

function getPdfNode(id, name, url) {
  var safe = safeUrl(url);

  var $wrap = $('<div>', {
    class: 'col-md-12 userdiv active pdffile pdf' + id,
    css: { paddingTop: '10px' }
  });

  var $icon = $('<div>', { class: 'col-md-2 pointer_on_hover' })
    .append($('<img>', {
      src: '../img/pdf-icon.png',
      css: { marginTop: '6px', width: '25px', height: '25px' }
    }));

  var $name = $('<div>', { class: 'col-md-9 pointer_on_hover' })
    .text(name || '-');

  var clickHandler = function () {
    if (safe) pdfclick(encodeURIComponent(safe));
  };
  $icon.on('click', clickHandler);
  $name.on('click', clickHandler);

  var $menu = $(
    "<div class='col-md-1' style='text-align: right; padding-top: 5px;'>" +
      "<div class='dropdown'>" +
        "<a style='text-decoration: none; color:#636b6f; width: 100%;' class='fa fa-ellipsis-v fa-2x dropdown-toggle pointer_on_hover' aria-hidden='true' type='button' data-toggle='dropdown'></a>" +
        "<ul class='dropdown-menu'>" +
          "<li><a class='removePdf pointer_on_hover'>Kaldır</a></li>" +
        "</ul>" +
      "</div>" +
    "</div>"
  );

  $menu.find('.removePdf').attr('data', String(id));

  $wrap.append($icon, $name, $menu);
  return $wrap;
}


function pdfupload(input) {
    var form_data = new FormData(); // Form data objesini çağır
    form_data.append("newsId", $("[id='news_id']").val());
    form_data.append("pdfFile", input.files[0]);
    var filename = $("#pdf" + fileno_pdf).val().split('\\').pop();
    $.ajax({
        url: 'addpdf',
        type: "POST",
        dataType: "json",
        data: form_data,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data',
        cache: false,
        success: function (data) {
            if (data == -1) {
                alert("Pdf boyutu 10MB'dan büyük olmamalı.")
            }
            else {
                $("[name='pdf_list']").append(getPdfNode(data.id, filename, data.url));
                $("[name='pdf_list']").show();
            }
        },
        error: function (request, error) {
            console.log(arguments);
        }
    });
    fileno_pdf++;
};

$("[name='pdf_list']").on('click', '.removePdf', function () {
    var docId = $(this).attr('data');
    $.ajax({
        url: 'deletepdf',
        type: "POST",
        dataType: "json",
        data: {
            documentId: docId
        },
        success: function (data) {
            if (data == -1) {
                alert("Beklenmedik bir hata oluştu. Lütfen daha sonra yeniden deneyin.")
            }
            else {
                $(".pdf" + docId).remove();
            }
        },
        error: function (request, error) {
            console.log(arguments);
        }
    });
});

//PDF END



function getNewsList() {
    currentRequest = $.ajax({
        url: 'getnewslist',
        type: "POST",
        dataType: "json",
        data: {
            pageNumber: 1,
            searchText: $("[name='searchNews']").val(),
            sortType: $("[name='sortBy']").val()
        },
	beforeSend: function () {
            if (currentRequest != null) {
                currentRequest.abort();
            }
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                for (var a = 0; a < data.length; a++) {
                    var html = "";
                    html += "<a href='editnews-" + data[a].id + "' style='color: inherit; text-decoration: inherit'><div class='col-md-12 eventdiv";
                    if (data[a].status == 1)
                        html += " active ";
                    else
                        html += " passive ";
                    html += "news" + newssayi++ + "' id='" + data[a].id + "'> <div class='col-md-2'><img src='";
                    if (data[a].image == null)
                        html += "../img/placeholder_event.jpg";
                    else
                        html += data[a].image;

                    html += "' class='col-md-12' style='border-radius: 4px; padding: 0px;'></div> <div class='col-md-2' style='padding-top: 18px; padding-bottom: 18px;'>";
                    if (data[a].title == null)
                        html += "-";
                    else
                        html += data[a].title;
                    html += "</div> <div class='col-md-1' style='padding-top: 18px; padding-bottom: 18px;'>";

                    if (data[a].type == null)
                        html += "-";
                    else if (data[a].type == 2)
                        html += "Event";
                    else if (data[a].type == 3)
                        html += "Discounts";
                    else if (data[a].type == 4)
                        html += "Farewell";
                    else if (data[a].type == 5)
                        html += "Death";
                    else if (data[a].type == 6)
                        html += "Other";
                    else if (data[a].type == 7)
                        html += "News";
                    else if (data[a].type == 8)
                        html += "Useful Links";
                    else if (data[a].type == 9)
                        html += "Contacts";
                    html += "</div> <div class='col-md-2' style='padding-top: 18px; padding-bottom: 18px;'>";

                    if (data[a].discount_type == null)
                        html += "-";
                    else if (data[a].discount_type == 2)
                        html += "Education and Personel Development";
                    else if (data[a].discount_type == 3)
                        html += "Event and Entertainment";
                    else if (data[a].discount_type == 4)
                        html += "Home and Life";
                    else if (data[a].discount_type == 5)
                        html += "Gastronomy and Food";
                    else if (data[a].discount_type == 6)
                        html += "Real Estate";
                    else if (data[a].discount_type == 7)
                        html += "Clothing and Fashion";
                    else if (data[a].discount_type == 8)
                        html += "Automotive";
                    else if (data[a].discount_type == 9)
                        html += "Health and Sports";
                    else if (data[a].discount_type == 12)
                        html += "Tourism and Car Rental";
                    else if (data[a].discount_type == 13)
                        html += "Other";
                    else if (data[a].discount_type == 14)
                        html += "Technology and Telecom";
                    html += "</div> <div class='col-md-2' style='padding-top: 18px; padding-bottom: 18px;'>";
                    if (data[a].start_time == null)
                        html += "-";
                    else
                        html += data[a].start_time;
                    if (data[a].end_time == null)
                        html += "";
                    else
                        html += "-" + data[a].end_time;

                    html += "</div> <div class='col-md-2' style='padding-top: 18px; padding-bottom: 18px;'>";
                    if (data[a].created_at == null)
                        html += "-";
                    else
                        html += data[a].created_at;

                    html += "</div> <input type='hidden' id='companyDetails-" + data[a].id + "' value='";
                    if (data[a].employee_type != null)
                        html += data[a].employee_type_name + "<br/>";
                    if (data[a].company_location_id != null)
                        html += data[a].location_names + "<br/>";
                    if (data[a].company_code != null)
                        html += data[a].company_names;
                    html += "'> <div class='dropdown'> <div class='col-md-1' style='text-align: right; padding-top: 13px; padding-bottom: 13px;'> <a style='text-decoration: none; color:#636b6f; width: 100%; width: 100%;' class='fa fa-ellipsis-v fa-2x dropdown-toggle' aria-hidden='true' type='button' data-toggle='dropdown'></a> <ul class='dropdown-menu'> <li><a href='editnews-" + data[a].id + "'>Edit</a></li>";
                    if (data[a].status == 1)
                        html += "<li class='sendnotifli' data='" + data[a].id + "'><a data-toggle='modal' data-target='#sendnotif' class='sendnotif' data='" + data[a].id + "'>Send Notification</a></li><li><a class='disable' data='" + data[a].id + "'>Passive</a></li>";
                    else if (data[a].status == 2)
                        html += "<li class='sendnotifli' data='" + data[a].id + "' style='display: none;'><a data-toggle='modal' data-target='#sendnotif' class='sendnotif' data='" + data[a].id + "'>Send Notification</a></li><li><a class='enable' data='" + data[a].id + "'>Active</a></li>";
                    html += "<li><a class='remove' data='" + data[a].id + "'><b>Delete</b></a></li> </ul> </div> </div> </div></a>";
                    $("#allNews").append(html);
                }
            }
        },
        error: function (request, error) {
            console.log(arguments);
        }
    });
}

var newssayi = 1;

function encodeHTML(s) {
    return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/"/g, '&quot;');
}

function ISODateString(d) {
    function pad(n) {
        return n < 10 ? '0' + n : n
    }

    d = d.addHours(3);
    return d.getUTCFullYear() + '-'
        + pad(d.getUTCMonth() + 1) + '-'
        + pad(d.getUTCDate()) + 'T'
        + pad(d.getUTCHours()) + ':'
        + pad(d.getUTCMinutes())
}

Date.prototype.addHours = function (h) {
    this.setTime(this.getTime() + (h * 60 * 60 * 1000));
    return this;
}

$(document).ready(function () {
    $.ajaxSetup({
        headers: {
            'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content')
        }
    });

    var $myDiv = $('#allNews');
    if ($myDiv.length) {
        getNewsList();
    }

    j = 5;
    var pagenumber = 2;

    $(window).on('scroll', function () {
        $('div.eventdiv').each(function () {
            // Is this element visible onscreen?
            var visible = $('div.news' + j).visible('partial');
            if (visible == true) {
                if (document.getElementsByClassName("news" + (j + 5)).length >= 1) {
                    console.log("pager geldi")
                    $.ajax({
                        url: 'getnewslist',
                        type: "POST",
                        dataType: "json",
                        data: {
                            pageNumber: pagenumber,
                            searchText: $("[name='searchNews']").val(),
                            sortType: $("[name='sortBy']").val()
                        },
                        success: function (data) {
                            if (data == -1) {
                                alert("Unexpected error. Please try again later.")
                            } else {
                                for (var a = 0; a < data.length; a++) {
                                    var html = "";
                                    html += "<a href='editnews-" + data[a].id + "' style='color: inherit; text-decoration: inherit'><div class='col-md-12 eventdiv";
                                    if (data[a].status == 1)
                                        html += " active ";
                                    else
                                        html += " passive ";
                                    html += "news" + newssayi++ + "' id='" + data[a].id + "'> <div class='col-md-2'><img src='";
                                    if (data[a].image == null)
                                        html += "../img/placeholder_event.jpg";
                                    else
                                        html += data[a].image;

                                    html += "' class='col-md-12' style='border-radius: 4px; padding: 0px;'></div> <div class='col-md-2' style='padding-top: 18px; padding-bottom: 18px;'>";

                                    html += "</div> <div class='col-md-1' style='padding-top: 18px; padding-bottom: 18px;'>";

                                    if (data[a].type == null)
                                        html += "-";
                                    else if (data[a].type == 2)
                                        html += "Event";
                                    else if (data[a].type == 3)
                                        html += "Discounts";
                                    else if (data[a].type == 4)
                                        html += "Farewell";
                                    else if (data[a].type == 5)
                                        html += "Death";
                                    else if (data[a].type == 6)
                                        html += "Other";
                                    else if (data[a].type == 7)
                                        html += "News";
                                    else if (data[a].type == 8)
                                        html += "Useful Links";
                                    else if (data[a].type == 9)
                                        html += "Contacts";
                                    html += "</div> <div class='col-md-2' style='padding-top: 18px; padding-bottom: 18px;'>";

                                    if (data[a].discount_type == null)
                                        html += "-";
                                    else if (data[a].discount_type == 2)
                                        html += "Education and Personel Development";
                                    else if (data[a].discount_type == 3)
                                        html += "Event and Entertainment";
                                    else if (data[a].discount_type == 4)
                                        html += "Home and Life";
                                    else if (data[a].discount_type == 5)
                                        html += "Gastronomy and Food";
                                    else if (data[a].discount_type == 6)
                                        html += "Real Estate";
                                    else if (data[a].discount_type == 7)
                                        html += "Clothing and Fashion";
                                    else if (data[a].discount_type == 8)
                                        html += "Automotive";
                                    else if (data[a].discount_type == 9)
                                        html += "Health and Sports";
                                    else if (data[a].discount_type == 12)
                                        html += "Tourism and Car Rental";
                                    else if (data[a].discount_type == 13)
                                        html += "Other";
                                    else if (data[a].discount_type == 14)
                                        html += "Technology and Telecom";
                                    html += "</div> <div class='col-md-2' style='padding-top: 18px; padding-bottom: 18px;'>";
                                    if (data[a].start_time == null)
                                        html += "-";
                                    else
                                        html += data[a].start_time;
                                    if (data[a].end_time == null)
                                        html += "";
                                    else
                                        html += "-" + data[a].end_time;

                                    html += "</div> <div class='col-md-2' style='padding-top: 18px; padding-bottom: 18px;'>";
                                    if (data[a].created_at == null)
                                        html += "-";
                                    else
                                        html += data[a].created_at;

                                    html += "</div> <input type='hidden' id='companyDetails-" + data[a].id + "' value='";
                                    if (data[a].employee_type != null)
                                        html += data[a].employee_type_name + "<br/>";
                                    if (data[a].company_location_id != null)
                                        html += data[a].location_names + "<br/>";
                                    if (data[a].company_code != null)
                                        html += data[a].company_names;
                                    html += "'> <div class='dropdown'> <div class='col-md-1' style='text-align: right; padding-top: 13px; padding-bottom: 13px;'> <a style='text-decoration: none; color:#636b6f; width: 100%; width: 100%;' class='fa fa-ellipsis-v fa-2x dropdown-toggle' aria-hidden='true' type='button' data-toggle='dropdown'></a> <ul class='dropdown-menu'> <li><a href='editnews-" + data[a].id + "'>Edit</a></li>";
                                    if (data[a].status == 1)
                                        html += "<li class='sendnotifli' data='" + data[a].id + "'><a data-toggle='modal' data-target='#sendnotif' class='sendnotif' data='" + data[a].id + "'>Send Notification</a></li><li><a class='disable' data='" + data[a].id + "'>Passive</a></li>";
                                    else if (data[a].status == 2)
                                        html += "<li class='sendnotifli' data='" + data[a].id + "' style='display: none;'><a data-toggle='modal' data-target='#sendnotif' class='sendnotif' data='" + data[a].id + "'>Send Notification</a></li><li><a class='enable' data='" + data[a].id + "'>Active</a></li>";
                                    html += "<li><a class='remove' data='" + data[a].id + "'><b>Delete</b></a></li> </ul> </div> </div> </div></a>";
                                    $("#allNews").append(html);
                                }
                            }
                        },
                        error: function (request, error) {
                            console.log(arguments);
                        }
                    });
                }
                j = j + 20;
                pagenumber++;
            }
        });
    });

    if (document.getElementById("newsData") && $("#newsData").val() != "") {
        var data = JSON.parse($("#newsData").val());

        $("[name='type']").val(data.type);
        showDiscType();

        if (data.company_location_id != null) {
            var numbers = JSON.stringify($newsData.location_ids).replace(/\"/g, "").split(',');
            var list = new Array();
            for(var i = 0; i < numbers.length; i++)
            {
                list.push(numbers[i]);
            }
            $("[name='locationIdList[]']").selectpicker('val', list);
        }
        if (data.company_code != null) {
            var numbers = JSON.stringify(data.company_ids).replace(/\"/g, "").split(',');
            var list = new Array();
            for(var i = 0; i < numbers.length; i++)
            {
                list.push(numbers[i]);
            }
            $("[name='companyIdList[]']").selectpicker('val', list);
        }
        if ($("[name='type']").val() != 3) {
            $("[name='discountTypeDiv']").empty();
        } else {
            $("[name='discountTypeDiv']").append("<label class='col-md-12 translation_title' for='discountType'>DISCOUNT TYPE</label> <select name='discountType' class='col-md-12 form-control' style='height: 45px;'> <option value='2'>Education and Personel Development</option> <option value='3'>Event and Entertainment</option> <option value='4'>Home and Life</option> <option value='5'>Gastronomy and Food</option> <option value='6'>Real Estate</option> <option value='7'>Clothing and Fashion</option> <option value='8'>Automotive</option> <option value='9'>Health and Sports</option> <option value='12'>Tourism and Car Rental</option> <option value='14'>Technology and Telecom</option> <option value='13'>Other</option> </select>");
        }
        $("[name='discountType']").val(data.discount_type);
        if (data.start_time != null) {
            $("[name='startTime']").val(ISODateString(new Date(data.start_time)));
        }
        if (data.end_time != null) {
            $("[name='endTime']").val(ISODateString(new Date(data.end_time)));
        }
        if(data.images){
            $image_array = data.images.split(',');
            for (var $i = 0; $i < $image_array.length; $i++) {
                $("input[name='imageString"+ ($i==0?'':$i) +"']").val($image_array[$i]);
            };    
        }
        imageInit();
        pdfStr = data.pdfs;
        if (pdfStr != null && pdfStr!=""){
            pdfs = JSON.parse(pdfStr);
            for (var a = 0; a < pdfs.length; a++) {
                var html = getPdfNode(pdfs[a].id, pdfs[a].name, pdfs[a].pdf);
                $("[name='pdf_list']").append(html);                                
                $("[name='pdf_list']").show();
            }

        }
        discountCodesInit(data);
    }
                

    if (document.getElementById("socialClubsDiv")) {
        socialClubsSetup()
    }
    if (document.getElementById("phonesDiv")) {
        phonesSetup()
    }
    if (document.getElementById("socialMediaDiv")) {
        mediasSetup()
    }
});

var currentRequest = null;
$("[name='searchNews']").keyup(function () {
    $("#allNews").empty();
    getNewsList();
});

$("#allNews").on("click", ".remove", (function () {
    var id = parseInt($(this).attr("data"));
    var result = confirm("Do you want to delete?");
    if (result) {
        $.ajax({
            url: 'deletenews',
            type: "POST",
            dataType: "json",
            data: {
                id: id
            },
            success: function (data) {
                if (data == -1) {
                    alert("Unexpected error. Please try again later.")
                } else {
                    $("#" + id).remove();
                }
            },
            error: function (request, error) {
                console.log(arguments);
            }
        });
    }
}));
$("#allNews").on("click", ".enable", (function () {
    var id = parseInt($(this).attr("data"));
    $.ajax({
        url: 'enablenews',
        type: "POST",
        dataType: "json",
        data: {
            id: id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                $("#" + id).attr("class", "col-md-12 eventdiv active");
            }
        },
        error: function (request, error) {
            console.log(arguments);
        }
    });
    $(this).attr("class", "disable");
    $(this).html("Passive");
    $(".sendnotifli[data='" + id + "']").show();
}));
$("#allNews").on("click", ".disable", (function () {
    var id = parseInt($(this).attr("data"));
    $.ajax({
        url: 'disablenews',
        type: "POST",
        dataType: "json",
        data: {
            id: id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                $("#" + id).attr("class", "col-md-12 eventdiv passive");
            }
        },
        error: function (request, error) {
            console.log(arguments);
        }
    });
    $(this).attr("class", "enable");
    $(this).html("Active");
    $(".sendnotifli[data='" + id + "']").hide();
}));

$("#allNews").on("click", ".sendnotif", (function () {
    var id = parseInt($(this).attr("data"));
    $("#sendnotif").attr('data', id);
    if ($("#companyDetails-" + id).attr("value") != '')
        $("#notifType").html($("#companyDetails-" + id).attr("value"));
    else
        $("#notifType").html("Everyone");
}));

$(".collartype").click(function () {
    var id = parseInt($("#sendnotif").attr("data"));

    $.ajax({
        url: 'sendnotification',
        type: "POST",
        dataType: "json",
        data: {
            id: id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else if (data == -2) {
                alert("The news is passive.")
            } else {
                alert("Push notification sent successfully.");
            }
        },
        error: function (request, error) {
            console.log(arguments);
        }
    });
});

function showDiscType() {
    if ($("[name='type']").val() != 3) {
        $("[name='discountTypeDiv']").empty();
        $("#discounts_code_div").hide();
    } else {
        $("[name='discountTypeDiv']").append("<label class='col-md-12 translation_title' for='discountType'>DISCOUNT TYPE</label> <select name='discountType' class='col-md-12 form-control' style='height: 45px;'> <option value='2'>Education and Personel Development</option> <option value='3'>Event and Entertainment</option> <option value='4'>Home and Life</option> <option value='5'>Gastronomy and Food</option> <option value='6'>Real Estate</option> <option value='7'>Clothing and Fashion</option> <option value='8'>Automotive</option> <option value='9'>Health and Sports</option> <option value='12'>Tourism and Car Rental</option> <option value='14'>Technology and Telecom</option> <option value='13'>Other</option> </select>");
        $("#discounts_code_div").show();
    }
    if ($("[name='type']").val() == 8 || $("[name='type']").val() == 9) {
        $(".cropit-preview").attr("style", "width: 780px; height: 172px; cursor: move; position: relative;");
        $("#textDiv").hide();
    } else if ($("[name='type']").val() == 10) {
        $("#typeDiv").hide();
        $("#dateDiv").hide();
        $(".cropit-preview").attr("style", "width: 470px; height: 336px; cursor: move; position: relative;");
    }else {
        $(".cropit-preview").attr("style", "width: 470px; height: 336px; cursor: move; position: relative;");
        $("#textDiv").show();
    }
	 $("#phoneNumber").hide();
    /*
    if ($("[name='type']").val() == 8 || $("[name='type']").val() == 9 || $("[name='type']").val() == 3)
        $("#AddCoverPhotoDiv").show();
    else
        $("#AddCoverPhotoDiv").hide();
    */
}


function discountCodesInit (data) {
    $("[name='discountCodeType']").val(data.discount_code_type);
    $("[name='oneForAllCodeInput']").val(data.discount_code_all);
    showDiscCodeType();
    if (data.discount_code_type == 2){
        showAndFillDiscountCodeMetrics(data.codeCount, data.usedCodeCount);
    }
}

function showDiscCodeType() {
    var newsType = $("[name='type']").val();
    if ( newsType== 3) {   //Discount type
        $("#discounts_code_div").show();
        var discountCodeType = $("[name='discountCodeType']").val();
        if ( discountCodeType == '0') {
            $("#one_for_all_div").hide();
            $("#one_for_each_div").hide();
        } else if (discountCodeType == '1') {
            //TBD Check if already used codes exist. if exists give warning
            //TBD Check if already loaded codes exist. if exists give warning
            $("#one_for_all_div").show();
            $("#one_for_each_div").hide();
        } else if (discountCodeType == '2') {
            $("#one_for_all_div").hide();
            $("#one_for_each_div").show();
        }
    } else{
        $("#discounts_code_div").hide();
    }
}
$("[name='upload_discountcodes_button']").click(function () {
    $("#discountCodeFile").click()
});

function discountCodeFileSelected(input) {

    var file = input.files[0];

    if (file) {
        var reader = new FileReader();
        reader.readAsText(file, "UTF-8");
        reader.onload = function (evt) {
            codes = evt.target.result.split("\n");
            var form_data = new FormData(); 
            form_data.append("newsId", $("[id='news_id']").val());
            form_data.append("discountCodes", codes);
		
	    var countCodes = 0
            for (var i = 0; i < codes.length; i++) {
		if(codes[i].length>2){
		    countCodes++
                    form_data.append('discountCodes[]', codes[i]);
		}
            }

            $.ajax({
                url: 'addDiscountCodes',
                type: "POST",
                dataType: "json",
                data: form_data,
                processData: false,
                contentType: false,
                enctype: 'multipart/form-data',
                cache: false,
                success: function (data) {
                    if (data == -1) {
                        alert("Indirim kodu en az 1 en fazla 10.000 adet olmali!")
                    }
                    else {
                        //Indirim kodu istatistigi
                        showAndFillDiscountCodeMetrics(countCodes, 0);
                    }
                },
                error: function (request, error) {
                    console.log(arguments);
                }
            });

        }
        reader.onerror = function (evt) {
            alert("error reading file")
        }
    }
};

function showAndFillDiscountCodeMetrics(countCode, countUsed){
    $("#discount_code_metrics").show();
    $("#discount_code_count").text(countCode);
    $("#discount_code_used").text(countUsed);
}

//SOCIAL CLUBS
function socialClubsSetup(){
    setupCollapsible()
}

function setupCollapsible(){
    var coll = document.getElementsByClassName("sc_collapsible");
    var i;
    
    for (i = 0; i < coll.length; i++) {
        setupCollapsibleOnClick(coll[i])
    }

    coll = document.getElementsByClassName("sc2_collapsible");
    for (i = 0; i < coll.length; i++) {
        setupCollapsible2OnClick(coll[i])
    }
}
function setupCollapsibleOnClick(e){
    e.addEventListener("click", function(event) {
        var targetElement = event.target || event.srcElement;
        if($(targetElement).is("div")) {
            this.classList.toggle("sc_active");
            var content = this.nextElementSibling;
            
            if (content.style.display === "block") {
            content.style.display = "none";
            } else {
            content.style.display = "block";
            }
        }
    });
}      

function setupCollapsible2OnClick(e){
    e.addEventListener("click", function(event) {
        var targetElement = event.target || event.srcElement;
        if($(targetElement).is("div")) {
            this.classList.toggle("sc2_active");
            var content = this.nextElementSibling;
            if (content.style.display === "block") {
            content.style.display = "none";
            } else {
            content.style.display = "block";
            }
        }
    });
}


function onChangeClubLoc(target){
    var id = parseInt(target.getAttribute('data'));
    var newValue = target.value;
    
    $.ajax({
        url: 'updateClubLoc',
        type: "POST",
        dataType: "json",
        data: {
            id: id,
            name: newValue
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
                window.location.replace('socialclubs')
            } else {
                console.log("Club location updated successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
            window.location.replace('socialclubs')
        }
    });
}

function onChangeClub(target){
    var id = parseInt(target.getAttribute('data'));
    var newValue = target.value;
    
    $.ajax({
        url: 'updateClub',
        type: "POST",
        dataType: "json",
        data: {
            id: id,
            name: newValue
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
                window.location.replace('socialclubs')
            } else {
                console.log("Club name updated successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
            window.location.replace('socialclubs')
        }
    });
}

function onChangeClubPersonName(target){
    var id = parseInt(target.getAttribute('data'));
    var newValue = target.value;
    
    $.ajax({
        url: 'updateClubPersonName',
        type: "POST",
        dataType: "json",
        data: {
            id: id,
            name: newValue
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
                window.location.replace('socialclubs')
            } else {
                console.log("Club responsible name updated successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
            window.location.replace('socialclubs')
        }
    });
}

function onChangeClubPersonContact(target){
    var id = parseInt(target.getAttribute('data'));
    var newValue = target.value;
    
    $.ajax({
        url: 'updateClubPersonContact',
        type: "POST",
        dataType: "json",
        data: {
            id: id,
            name: newValue
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
                window.location.replace('socialclubs')
            } else {
                console.log("Club responsible name updated successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
            window.location.replace('socialclubs')
        }
    });
}

function onClickAddClubLoc(target){
    $.ajax({
        url: 'addClubLoc',
        type: "POST",
        dataType: "json",
        data: {
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                addEmptyClubLocation (data, target)
                console.log("Club loc added successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
    return false
}

function addEmptyClubLocation(data, target){
  // 1) ID kesinlikle sayı olsun (Snyk’in “tainted” flow’unu da keser)
  var id = parseInt(data, 10);
  if (isNaN(id)) return;

  // 2) DOM node üret
  var $locDiv = $('<div>', { id: 'locDiv-' + id });

  var $collapsible = $('<div>', {
    id: 'locCollapsibleDiv-' + id,
    'class': 'row sc_collapsible',
    css: { paddingTop:'10px', paddingBottom:'10px', paddingRight:'0px', marginLeft:'0px' }
  });

  var $col12 = $('<div>', {
    'class': 'col-md-12',
    css: { width:'calc(100% - 18px)', paddingRight:'0px', paddingLeft:'0px' }
  });

  var $col11 = $('<div>', { 'class': 'col-md-11', css: { paddingLeft:'0px' } });

  var $input = $('<input>', {
    type: 'text',
    'class': 'sc-input-text',
    placeholder: 'ex: Aksaray',
    name: 'clubLoc',
    maxlength: 100
  }).attr('data', id).on('change', function(){
    onChangeClubLoc(this);
  });

  $col11.append($input);

  // Dropdown + Delete (inline javascript: yerine click handler)
  var $dropdown = $('<div>', { 'class': 'dropdown col-md-1', css: { textAlign:'right' } });

  var $toggle = $('<a>', {
    'class': 'fa fa-ellipsis-v fa-2x dropdown-toggle',
    'aria-hidden': 'true',
    type: 'button',
    'data-toggle': 'dropdown',
    css: { textDecoration:'none', color:'#636b6f', paddingLeft:'18px', paddingRight:'18px' }
  }).on('click', function(e){ e.preventDefault(); });

  var $menu = $('<ul>', { 'class': 'dropdown-menu' });
  var $li = $('<li>');
  var $del = $('<a>', { 'class': 'remove', href: '#'}).append($('<b>').text('Delete'))
    .on('click', function(e){
      e.preventDefault();
      onClickDeleteClubLoc(id);
    });

  $li.append($del);
  $menu.append($li);

  $dropdown.append($toggle, $menu);

  $col12.append($col11, $dropdown);
  $collapsible.append($col12);

  // Content kısmı
  var $content = $('<div>', { 'class': 'content sc1_content', css: { display:'none' } });

  var $addNew = $('<a>', {
    name: 'addNewClub',
    href: '#',
    css: { margin:'18px', display:'inline-block' }
  }).append($('<i>', { 'class': 'fa fa-plus-circle', 'aria-hidden':'true' }))
    .append(' Add New Club')
    .on('click', function(e){
      e.preventDefault();
      onClickAddClub(id);
    });

  var $clubsDiv = $('<div>', { id: 'clubsDivForLoc-' + id });

  $content.append($addNew, $clubsDiv);

  $locDiv.append($collapsible, $content);

  // 3) DOM’a bas
  $('#clubLocsDiv').prepend($locDiv);

  // 4) Mevcut fonksiyonun beklentisi aynı kalsın
  setupCollapsibleOnClick(document.getElementById('locCollapsibleDiv-' + id));
}

function onClickDeleteClubLoc(loc_id){
    var id = parseInt(loc_id);
    $.ajax({
        url: 'deleteClubLoc',
        type: "POST",
        dataType: "json",
        data: {
            id: id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                $("#locDiv-" + id).remove();
                console.log("Club loc deleted successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
}


function onClickAddClub(loc_id){
    $.ajax({
        url: 'addClub',
        type: "POST",
        dataType: "json",
        data: {
            loc_id: loc_id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                addEmptyClub (data, loc_id)
                console.log("Club added successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
    return false
}

function addEmptyClub(data, loc_id){
    // ✅ allowlist: sadece integer ID
    var id = parseInt(data, 10);
    if (isNaN(id)) return;

    var loc = parseInt(loc_id, 10);
    if (isNaN(loc)) return;

    var $clubDiv = $('<div>', { id: 'clubDiv-' + id });

    var $collapsible = $('<div>', {
        id: 'clubCollapsibleDiv-' + id,
        'class': 'row sc2_collapsible',
        style: 'padding-top: 10px; padding-bottom: 10px; padding-right:0px; margin-left:0px;'
    });

    var $col12 = $('<div>', {
        'class': 'col-md-12',
        style: 'width: calc(100% - 18px); padding-right: 0px; padding-left:0px;'
    });

    var $col11 = $('<div>', { 'class': 'col-md-11', style: 'padding-left:0px;' });

    var $input = $('<input>', {
        type: 'text',
        'class': 'sc-input-text',
        placeholder: 'ex: Bisiklet Kulubü',
        maxlength: 100
    }).attr('data', id).on('change', function(){
        onChangeClub(this);
    });

    $col11.append($input);

    // Dropdown (inline javascript: yerine handler)
    var $dropdown = $('<div>', { 'class': 'dropdown col-md-1', style: 'text-align: right;' });

    var $toggle = $('<a>', {
        href: '#',
        style: 'text-decoration: none; color:#636b6f; padding-left: 18px; padding-right: 18px;',
        'class': 'fa fa-ellipsis-v fa-2x dropdown-toggle',
        'aria-hidden': 'true',
        type: 'button',
        'data-toggle': 'dropdown'
    }).on('click', function(e){ e.preventDefault(); });

    var $menu = $('<ul>', { 'class': 'dropdown-menu' });
    var $li = $('<li>');
    var $del = $('<a>', { 'class': 'remove', href: '#' })
        .append($('<b>').text('Delete'))
        .on('click', function(e){
            e.preventDefault();
            onClickDeleteClub(id);
        });

    $li.append($del);
    $menu.append($li);

    $dropdown.append($toggle, $menu);

    $col12.append($col11, $dropdown);
    $collapsible.append($col12);

    // Content
    var $content = $('<div>', { 'class': 'sc2_content', style: 'display: none;' });

    var $addPerson = $('<a>', {
        href: '#',
        style: 'margin: 14px;display: inline-block;'
    }).append($('<i>', { 'class': 'fa fa-plus-circle', 'aria-hidden': 'true' }))
      .append(' Add New Person')
      .on('click', function(e){
          e.preventDefault();
          onClickAddClubDetail(id);
      });

    var $detailDiv = $('<div>', { id: 'detailDivForClub-' + id });

    $content.append($addPerson, $detailDiv);

    $clubDiv.append($collapsible, $content);

    $('#clubsDivForLoc-' + loc).prepend($clubDiv);

    setupCollapsible2OnClick(document.getElementById('clubCollapsibleDiv-' + id));
}

function onClickDeleteClub(id){
    var id = parseInt(id);
    $.ajax({
        url: 'deleteClub',
        type: "POST",
        dataType: "json",
        data: {
            id: id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                $("#clubDiv-" + id).remove();

                console.log("Club deleted successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
}

function onClickAddClubDetail(club_id){
    $.ajax({
        url: 'addClubDetail',
        type: "POST",
        dataType: "json",
        data: {
            club_id: club_id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                addEmptyClubDetail (data, club_id)
                console.log("Club detail added successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
}

function addEmptyClubDetail(data, club_id){
    // ✅ allowlist: sadece integer ID
    var id = parseInt(data, 10);
    if (isNaN(id)) return;

    var club = parseInt(club_id, 10);
    if (isNaN(club)) return;

    var $wrap = $('<div>', { id: 'clubDetailDiv-' + id });

    var $row = $('<div>', {
        'class': 'row',
        style: 'padding-top: 8px; padding-bottom: 8px;'
    });

    var $col12 = $('<div>', { 'class': 'col-md-12' });

    // Name input
    var $col6 = $('<div>', { 'class': 'col-md-6' });
    var $name = $('<input>', {
        type: 'text',
        'class': 'col-md-12 sc-input-text',
        placeholder: 'Name',
        name: 'respName',
        maxlength: 100
    }).attr('data', id).on('change', function(){
        onChangeClubPersonName(this);
    });
    $col6.append($name);

    // Contact input
    var $col5 = $('<div>', { 'class': 'col-md-5' });
    var $contact = $('<input>', {
        type: 'text',
        'class': 'col-md-12 sc-input-text',
        placeholder: 'ex: 05321234567',
        name: 'respContact',
        maxlength: 100
    }).attr('data', id).on('change', function(){
        onChangeClubPersonContact(this);
    });
    $col5.append($contact);

    // Dropdown delete (javascript: href yerine click handler)
    var $col1 = $('<div>', { 'class': 'col-md-1', style: 'text-align: right;' });
    var $dropdown = $('<div>', { 'class': 'dropdown' });

    var $toggle = $('<a>', {
        href: '#',
        style: 'text-decoration: none; color:#636b6f; padding-left: 18px; padding-right: 18px;',
        'class': 'fa fa-ellipsis-v fa-2x dropdown-toggle',
        'aria-hidden': 'true',
        type: 'button',
        'data-toggle': 'dropdown'
    }).on('click', function(e){ e.preventDefault(); });

    var $menu = $('<ul>', { 'class': 'dropdown-menu' });
    var $li = $('<li>');
    var $del = $('<a>', { 'class': 'remove', href: '#' })
        .append($('<b>').text('Delete'))
        .on('click', function(e){
            e.preventDefault();
            onClickDeleteClubDetail(id);
        });

    $li.append($del);
    $menu.append($li);

    $dropdown.append($toggle, $menu);
    $col1.append($dropdown);

    $col12.append($col6, $col5, $col1);
    $row.append($col12);
    $wrap.append($row);

    $('#detailDivForClub-' + club).prepend($wrap);
}

function onClickDeleteClubDetail(id){
    var id = parseInt(id);
    $.ajax({
        url: 'deleteClubDetail',
        type: "POST",
        dataType: "json",
        data: {
            id: id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                $("#clubDetailDiv-" + id).remove();

                console.log("Club detail deleted successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
}

$("[name='allClubs']").on("click", ".removeClub", (function () {
    var id = parseInt($(this).attr("data"));
    var result = confirm("Do you want to delete?");
    if (result) {
        $.ajax({
            url: 'deletenews',
            type: "POST",
            dataType: "json",
            data: {
                id: id
            },
            success: function (data) {
                if (data == -1) {
                    alert("Unexpected error. Please try again later.")
                } else {
                    $("#" + id).remove();
                }
            },
            error: function (request, error) {
                console.log(arguments);
            }
        });
    }
}));

// Social Clubs END


//SOCIAL MEDIA
function mediasSetup(){
    setupCollapsible()
  }
function onChangeMedia(target){
    var id = parseInt(target.getAttribute('data'));
    var newValue = target.value;
    
    $.ajax({
        url: 'updateMedia',
        type: "POST",
        dataType: "json",
        data: {
            id: id,
            name: newValue
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
                window.location.replace('medias')
            } else {
                console.log("Platform name updated successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
            window.location.replace('medias')
        }
    });
}
  
function onChangeMediaAccount(target){
    var id = parseInt(target.getAttribute('data'));
    var newValue = target.value;
    $.ajax({
        url: 'updateMediaAccount',
        type: "POST",
        dataType: "json",
        data: {
            id: id,
            account: newValue
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
                window.location.replace('medias')
            } else {
                console.log("Account name updated successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
            window.location.replace('medias')
        }
    });
}

function onChangeMediaUrl(target){
    var id = parseInt(target.getAttribute('data'));
    var newValue = target.value;

    $.ajax({
        url: 'updateMediaUrl',
        type: "POST",
        dataType: "json",
        data: {
            id: id,
            url: newValue
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
                window.location.replace('medias')
            } else {
                console.log("URL updated successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
            window.location.replace('medias')
        }
    });
}

function onClickAddMedia(target){
    $.ajax({
        url: 'addMedia',
        type: "POST",
        dataType: "json",
        data: {
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                addEmptyMedia (data, target)
                console.log("Platform added successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
    return false
}

function addEmptyMedia(data, target){
    // ✅ allowlist: sadece integer ID kabul et
    var id = parseInt(data, 10);
    if (isNaN(id)) return;

    var $mediaDiv = $('<div>', { id: 'mediaDiv-' + id });

    var $collapsible = $('<div>', {
        id: 'mediaCollapsibleDiv-' + id,
        'class': 'row sc_collapsible',
        style: 'padding-top: 10px; padding-bottom: 10px; padding-right:0px; margin-left:0px;'
    });

    var $col12 = $('<div>', {
        'class': 'col-md-12',
        style: 'width: calc(100% - 18px); padding-right: 0px; padding-left:0px;'
    });

    var $col11 = $('<div>', { 'class': 'col-md-11', style: 'padding-left:0px;' });

    var $input = $('<input>', {
        type: 'text',
        'class': 'sc-input-text',
        placeholder: 'ex: Instagram',
        maxlength: 100
    }).attr('data', id).on('change', function () {
        onChangeMedia(this);
    });

    $col11.append($input);

    // Dropdown + Delete (javascript: yerine click handler)
    var $dropdown = $('<div>', { 'class': 'dropdown col-md-1', style: 'text-align: right;' });

    var $toggle = $('<a>', {
        href: '#',
        style: 'text-decoration: none; color:#636b6f; padding-left: 18px; padding-right: 18px;',
        'class': 'fa fa-ellipsis-v fa-2x dropdown-toggle',
        'aria-hidden': 'true',
        type: 'button',
        'data-toggle': 'dropdown'
    }).on('click', function (e) { e.preventDefault(); });

    var $menu = $('<ul>', { 'class': 'dropdown-menu' });
    var $li = $('<li>');
    var $del = $('<a>', { 'class': 'remove', href: '#' })
        .append($('<b>').text('Delete'))
        .on('click', function (e) {
            e.preventDefault();
            onClickDeleteMedia(id);
        });

    $li.append($del);
    $menu.append($li);

    $dropdown.append($toggle, $menu);

    $col12.append($col11, $dropdown);
    $collapsible.append($col12);

    // Content (Add New Media Detail)
    var $content = $('<div>', { 'class': 'content sc1_content', style: 'display: none;' });

    var $addDetail = $('<a>', {
        href: '#',
        style: 'margin: 18px;display: inline-block;'
    }).append($('<i>', { 'class': 'fa fa-plus-circle', 'aria-hidden': 'true' }))
      .append(' Add New Media Detail')
      .on('click', function (e) {
          e.preventDefault();
          onClickAddMediaDetail(id);
      });

    var $detailDiv = $('<div>', { id: 'detailDivForMedia-' + id });

    $content.append($addDetail, $detailDiv);

    $mediaDiv.append($collapsible, $content);

    $('#mediasDiv').prepend($mediaDiv);

    setupCollapsibleOnClick(document.getElementById('mediaCollapsibleDiv-' + id));
}

function onClickDeleteMedia(loc_id){
    var id = parseInt(loc_id);
    $.ajax({
        url: 'deleteMedia',
        type: "POST",
        dataType: "json",
        data: {
            id: id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                $("#mediaDiv-" + id).remove();
                console.log("Platform deleted successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
}

function onClickAddMediaDetail(media_id){
    $.ajax({
        url: 'addMediaDetail',
        type: "POST",
        dataType: "json",
        data: {
            media_id: media_id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                addEmptyMediaDetail (data, media_id)
                console.log("Media detail added successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
}

function addEmptyMediaDetail(data, media_id){
    // ✅ allowlist: sadece integer ID
    var id = parseInt(data, 10);
    if (isNaN(id)) return;

    var media = parseInt(media_id, 10);
    if (isNaN(media)) return;

    var $detailWrap = $('<div>', { id: 'detailDiv-' + id });

    var $row = $('<div>', {
        'class': 'row',
        style: 'padding-top: 10px; padding-bottom: 10px; padding-right:0px; margin-left:0px;'
    });

    var $col12 = $('<div>', {
        'class': 'col-md-12',
        style: 'width: calc(100% - 18px); padding-right: 0px; padding-left:0px;'
    });

    // Account input
    var $col4 = $('<div>', { 'class': 'col-md-4', style: 'padding-left:0px;' });
    var $acc = $('<input>', {
        type: 'text',
        'class': 'sc-input-text',
        placeholder: 'ex: Mercedes-Benz Türk',
        maxlength: 100
    }).attr('data', id).on('change', function(){
        onChangeMediaAccount(this);
    });
    $col4.append($acc);

    // URL input
    var $col7 = $('<div>', { 'class': 'col-md-7' });
    var $url = $('<input>', {
        type: 'text',
        'class': 'sc-input-text',
        placeholder: 'ex: https://www.instagram.com/mercedesbenzturk/',
        maxlength: 100
    }).attr('data', id).on('change', function(){
        onChangeMediaUrl(this);
    });
    $col7.append($url);

    // Dropdown delete (javascript: href yerine click handler)
    var $dropCol = $('<div>', { 'class': 'dropdown col-md-1', style: 'text-align: right;' });

    var $toggle = $('<a>', {
        href: '#',
        style: 'text-decoration: none; color:#636b6f; padding-left: 18px; padding-right: 18px;',
        'class': 'fa fa-ellipsis-v fa-2x dropdown-toggle',
        'aria-hidden': 'true',
        type: 'button',
        'data-toggle': 'dropdown'
    }).on('click', function(e){ e.preventDefault(); });

    var $menu = $('<ul>', { 'class': 'dropdown-menu' });
    var $li = $('<li>');
    var $del = $('<a>', { 'class': 'remove', href: '#' })
        .append($('<b>').text('Delete'))
        .on('click', function(e){
            e.preventDefault();
            onClickDeleteMediaDetail(id);
        });

    $li.append($del);
    $menu.append($li);

    $dropCol.append($toggle, $menu);

    $col12.append($col4, $col7, $dropCol);
    $row.append($col12);
    $detailWrap.append($row);

    $('#detailDivForMedia-' + media).prepend($detailWrap);
}

function onClickDeleteMediaDetail(id){
    var id = parseInt(id);
    $.ajax({
        url: 'deleteMediaDetail',
        type: "POST",
        dataType: "json",
        data: {
            id: id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                $("#detailDiv-" + id).remove();
                console.log("Media details deleted successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
}
// Social Media END


  //PHONE NUMBERS
function phonesSetup(){
    setupCollapsible()
  }
  function onChangePhoneLoc(target){
    var id = parseInt(target.getAttribute('data'));
    var newValue = target.value;
    
    $.ajax({
        url: 'updatePhoneLoc',
        type: "POST",
        dataType: "json",
        data: {
            id: id,
            name: newValue
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
                window.location.replace('phones')
            } else {
                console.log("Phone location updated successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
            window.location.replace('phones')
        }
    });
  }
  
  function onChangeSantral(target){
    var id = parseInt(target.getAttribute('data'));
    var newValue = target.value;
    
    $.ajax({
        url: 'updateSantral',
        type: "POST",
        dataType: "json",
        data: {
            id: id,
            name: newValue
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
                window.location.replace('phones')
            } else {
                console.log("Phone location updated successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
            window.location.replace('phones')
        }
    });
  }

  function onChangePhone(target){
    var id = parseInt(target.getAttribute('data'));
    var newValue = target.value;
    
    $.ajax({
        url: 'updatePhone',
        type: "POST",
        dataType: "json",
        data: {
            id: id,
            name: newValue
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
                window.location.replace('phones')
            } else {
                console.log("Phone name updated successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
            window.location.replace('phones')
        }
    });
  }
  
  function onChangePhoneUnit(target){
    var id = parseInt(target.getAttribute('data'));
    var newValue = target.value;
    
    $.ajax({
        url: 'updatePhoneUnit',
        type: "POST",
        dataType: "json",
        data: {
            id: id,
            name: newValue
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
                window.location.replace('phones')
            } else {
                console.log("Phone unit name updated successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
            window.location.replace('phones')
        }
    });
  }
  
  function onChangePhoneNote(target){
    var id = parseInt(target.getAttribute('data'));
    var newValue = target.value;
    
    $.ajax({
        url: 'updatePhoneNote',
        type: "POST",
        dataType: "json",
        data: {
            id: id,
            name: newValue
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
                window.location.replace('phones')
            } else {
                console.log("Phone note updated successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
            window.location.replace('phones')
        }
    });
  }
  
  function onChangePhoneInternal(target){
    var id = parseInt(target.getAttribute('data'));
    var newValue = target.value;
    
    $.ajax({
        url: 'updatePhoneInternal',
        type: "POST",
        dataType: "json",
        data: {
            id: id,
            name: newValue
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
                window.location.replace('phones')
            } else {
                console.log("Phone internal updated successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
            window.location.replace('phones')
        }
    });
  }
  
  function onClickAddPhoneLoc(target){
    $.ajax({
        url: 'addPhoneLoc',
        type: "POST",
        dataType: "json",
        data: {
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                addEmptyPhoneLocation (data, target)
                console.log("Phone loc added successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
    return false
  }
  
function addEmptyPhoneLocation(data, target){
    // ✅ allowlist: sadece integer ID
    var id = parseInt(data, 10);
    if (isNaN(id)) return;

    var $locDiv = $('<div>', { id: 'locDiv-' + id });

    var $collapsible = $('<div>', {
        id: 'locCollapsibleDiv-' + id,
        'class': 'row sc_collapsible',
        style: 'padding-top: 10px; padding-bottom: 10px; padding-right:0px; margin-left:0px;'
    });

    var $col12 = $('<div>', {
        'class': 'col-md-12',
        style: 'width: calc(100% - 18px); padding-right: 0px; padding-left:0px;'
    });

    var $col11 = $('<div>', { 'class': 'col-md-11', style: 'padding-left:0px;' });

    var $input = $('<input>', {
        type: 'text',
        'class': 'sc-input-text',
        placeholder: 'ex: Aksaray',
        maxlength: 100
    }).attr('data', id).on('change', function(){
        onChangePhoneLoc(this);
    });

    $col11.append($input);

    // Dropdown + Delete (javascript: yerine click handler)
    var $dropdown = $('<div>', { 'class': 'dropdown col-md-1', style: 'text-align: right;' });

    var $toggle = $('<a>', {
        href: '#',
        style: 'text-decoration: none; color:#636b6f; padding-left: 18px; padding-right: 18px;',
        'class': 'fa fa-ellipsis-v fa-2x dropdown-toggle',
        'aria-hidden': 'true',
        type: 'button',
        'data-toggle': 'dropdown'
    }).on('click', function(e){ e.preventDefault(); });

    var $menu = $('<ul>', { 'class': 'dropdown-menu' });
    var $li = $('<li>');
    var $del = $('<a>', { 'class': 'remove', href: '#' })
        .append($('<b>').text('Delete'))
        .on('click', function(e){
            e.preventDefault();
            onClickDeletePhoneLoc(id);
        });

    $li.append($del);
    $menu.append($li);

    $dropdown.append($toggle, $menu);

    $col12.append($col11, $dropdown);
    $collapsible.append($col12);

    // Content (Add New Phone)
    var $content = $('<div>', { 'class': 'content sc1_content', style: 'display: none;' });

    var $addPhone = $('<a>', {
        href: '#',
        style: 'margin: 18px;display: inline-block;'
    }).append($('<i>', { 'class': 'fa fa-plus-circle', 'aria-hidden': 'true' }))
      .append(' Add New Phone')
      .on('click', function(e){
          e.preventDefault();
          onClickAddPhone(id);
      });

    var $phonesDiv = $('<div>', { id: 'phonesDivForLoc-' + id });

    $content.append($addPhone, $phonesDiv);

    $locDiv.append($collapsible, $content);

    $('#phoneLocsDiv').prepend($locDiv);

    setupCollapsibleOnClick(document.getElementById('locCollapsibleDiv-' + id));
}
  
  function onClickDeletePhoneLoc(loc_id){
    var id = parseInt(loc_id);
    $.ajax({
        url: 'deletePhoneLoc',
        type: "POST",
        dataType: "json",
        data: {
            id: id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                $("#locDiv-" + id).remove();
                console.log("Phone loc deleted successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
  }
  
  
  function onClickAddPhone(loc_id){
    $.ajax({
        url: 'addPhone',
        type: "POST",
        dataType: "json",
        data: {
            loc_id: loc_id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                addEmptyPhone (data, loc_id)
                console.log("Phone added successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
    return false
  }
  
function addEmptyPhone(data, loc_id){
    // ✅ allowlist: sadece integer ID
    var id = parseInt(data, 10);
    if (isNaN(id)) return;

    var loc = parseInt(loc_id, 10);
    if (isNaN(loc)) return;

    var $phoneDiv = $('<div>', { id: 'phoneDiv-' + id });

    var $collapsible = $('<div>', {
        id: 'phoneCollapsibleDiv-' + id,
        'class': 'row sc2_collapsible',
        style: 'padding-top: 10px; padding-bottom: 10px; padding-right:0px; margin-left:0px;'
    });

    var $col12 = $('<div>', {
        'class': 'col-md-12',
        style: 'width: calc(100% - 18px); padding-right: 0px; padding-left:0px;'
    });

    var $col11 = $('<div>', { 'class': 'col-md-11', style: 'padding-left:0px;' });

    var $input = $('<input>', {
        type: 'text',
        'class': 'sc-input-text',
        placeholder: 'ex: İtfaiye',
        maxlength: 100
    }).attr('data', id).on('change', function(){
        onChangePhone(this);
    });

    $col11.append($input);

    // Dropdown + Delete (javascript: yerine click handler)
    var $dropdown = $('<div>', { 'class': 'dropdown col-md-1', style: 'text-align: right;' });

    var $toggle = $('<a>', {
        href: '#',
        style: 'text-decoration: none; color:#636b6f; padding-left: 18px; padding-right: 18px;',
        'class': 'fa fa-ellipsis-v fa-2x dropdown-toggle',
        'aria-hidden': 'true',
        type: 'button',
        'data-toggle': 'dropdown'
    }).on('click', function(e){ e.preventDefault(); });

    var $menu = $('<ul>', { 'class': 'dropdown-menu' });
    var $li = $('<li>');
    var $del = $('<a>', { 'class': 'remove', href: '#' })
        .append($('<b>').text('Delete'))
        .on('click', function(e){
            e.preventDefault();
            onClickDeletePhone(id);
        });

    $li.append($del);
    $menu.append($li);

    $dropdown.append($toggle, $menu);

    $col12.append($col11, $dropdown);
    $collapsible.append($col12);

    // Content (Add New Phone Detail)
    var $content = $('<div>', { 'class': 'sc2_content', style: 'display: none;' });

    var $addDetail = $('<a>', {
        href: '#',
        style: 'margin: 14px;display: inline-block;'
    }).append($('<i>', { 'class': 'fa fa-plus-circle', 'aria-hidden': 'true' }))
      .append(' Add New Phone Detail')
      .on('click', function(e){
          e.preventDefault();
          onClickAddPhoneDetail(id);
      });

    var $detailDiv = $('<div>', { id: 'detailDivForPhone-' + id });

    $content.append($addDetail, $detailDiv);

    $phoneDiv.append($collapsible, $content);

    $('#phonesDivForLoc-' + loc).prepend($phoneDiv);

    setupCollapsible2OnClick(document.getElementById('phoneCollapsibleDiv-' + id));
}
  
  function onClickDeletePhone(id){
    var id = parseInt(id);
    $.ajax({
        url: 'deletePhone',
        type: "POST",
        dataType: "json",
        data: {
            id: id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                $("#phoneDiv-" + id).remove();
  
                console.log("Phone deleted successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
  }
  
  function onClickAddPhoneDetail(phone_id){
    $.ajax({
        url: 'addPhoneDetail',
        type: "POST",
        dataType: "json",
        data: {
            phone_id: phone_id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                addEmptyPhoneDetail (data, phone_id)
                console.log("Phone detail added successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
  }
  
function addEmptyPhoneDetail(data, phone_id){
    // ✅ allowlist: sadece integer ID
    var id = parseInt(data, 10);
    if (isNaN(id)) return;

    var phone = parseInt(phone_id, 10);
    if (isNaN(phone)) return;

    var $wrap = $('<div>', { id: 'phoneDetailDiv-' + id });

    var $row = $('<div>', {
        'class': 'row',
        style: 'padding-top: 8px; padding-bottom: 8px;'
    });

    var $col12 = $('<div>', { 'class': 'col-md-12' });

    // Unit
    var $col4a = $('<div>', { 'class': 'col-md-4' });
    var $unit = $('<input>', {
        type: 'text',
        'class': 'col-md-12 sc-input-text',
        placeholder: 'Unit',
        name: 'phoneUnit',
        maxlength: 100
    }).attr('data', id).on('change', function(){
        onChangePhoneUnit(this);
    });
    $col4a.append($unit);

    // Note
    var $col4b = $('<div>', { 'class': 'col-md-4' });
    var $note = $('<input>', {
        type: 'text',
        'class': 'col-md-12 sc-input-text',
        placeholder: 'Note',
        name: 'phoneNote',
        maxlength: 100
    }).attr('data', id).on('change', function(){
        onChangePhoneNote(this);
    });
    $col4b.append($note);

    // Internal comm.
    var $col3 = $('<div>', { 'class': 'col-md-3' });
    var $internal = $('<input>', {
        type: 'text',
        'class': 'col-md-12 sc-input-text',
        placeholder: 'Internal comm.',
        name: 'phoneInternal',
        maxlength: 100
    }).attr('data', id).on('change', function(){
        onChangePhoneInternal(this);
    });
    $col3.append($internal);

    // Dropdown delete (javascript: href yerine click handler)
    var $col1 = $('<div>', { 'class': 'col-md-1', style: 'text-align: right;' });
    var $dropdown = $('<div>', { 'class': 'dropdown' });

    var $toggle = $('<a>', {
        href: '#',
        style: 'text-decoration: none; color:#636b6f; padding-left: 18px; padding-right: 18px;',
        'class': 'fa fa-ellipsis-v fa-2x dropdown-toggle',
        'aria-hidden': 'true',
        type: 'button',
        'data-toggle': 'dropdown'
    }).on('click', function(e){ e.preventDefault(); });

    var $menu = $('<ul>', { 'class': 'dropdown-menu' });
    var $li = $('<li>');
    var $del = $('<a>', { 'class': 'remove', href: '#' })
        .append($('<b>').text('Delete'))
        .on('click', function(e){
            e.preventDefault();
            onClickDeletePhoneDetail(id);
        });

    $li.append($del);
    $menu.append($li);

    $dropdown.append($toggle, $menu);
    $col1.append($dropdown);

    $col12.append($col4a, $col4b, $col3, $col1);
    $row.append($col12);
    $wrap.append($row);

    $('#detailDivForPhone-' + phone).prepend($wrap);
}

  function onClickDeletePhoneDetail(id){
    var id = parseInt(id);
    $.ajax({
        url: 'deletePhoneDetail',
        type: "POST",
        dataType: "json",
        data: {
            id: id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                $("#phoneDetailDiv-" + id).remove();
  
                console.log("Phone detail deleted successfully!");
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
  }
  
// Phone Numbers END


//APP FEEDBACK START
  var offsetVal = 20;

function onClickLoadMore(){
    $.ajax({
        url: 'getFeedbacks',
        type: "POST",
        dataType: "json",
        data: {
            offset: offsetVal
        },
        success: function (data) {
            if (data.length < 20) { //finished
                //hide button
                $("#loadmore").hide()
            } 
            for (var a = 0; a < data.length; a++) {

                var $item = $('<div>', { 'class': 'col-md-12 eventdiv passive' });

                $item.append($('<label>').text('Posted by : '));
                $item.append($('<label>').text(data[a].user == null ? '' : String(data[a].user)));

                $item.append(
                $('<label>')
                    .css('float', 'right')
                    .text('Date : ' + (data[a].created_at == null ? '' : String(data[a].created_at)))
                );

                $item.append('<br>');

                $item.append(
                $('<label>').text(data[a].text == null ? '' : String(data[a].text))
                );

                $item.append('<br>');

                $("#feedbackDiv").append($item);

            }
            offsetVal+=data.length
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
}

function onChangeEmails(target){

    var newValue = target.value;
    $.ajax({
        url: 'updateEmails',
        type: "POST",
        dataType: "json",
        data: {
            emails: newValue
        },
        success: function (data) {
            if (data==null) {
                alert("Unexpected error. Please try again later.")
            } else {
                if (!data.success){
                    alert(data.errorMessage)
                }else
                    alert("Successfull")
            }
        },
        error: function (request, error) {
            alert("Unexpected error. Please try again later.")
        }
    });
}

//APP FEEDBACK END

function onClickSubmitForm(){

    //Logical checks for Discount Codes
    fd = new FormData($("#newsForm")[0]);
    $.ajax({
        url: 'editnews',
        type: "POST",
        data: fd,
        processData: false,
        contentType: false,
        enctype: 'multipart/form-data',
        cache: false,
        success: function (data) {
            retVal = JSON.parse(data);

            if (retVal.responseData != 0) {
                alert (retVal.errorMessage)
            } else {
                if ($("[name='type']").val() == 10) 
                    window.location.replace('socialclubs')
                else
                    window.location.replace('home')
            }
        },
        error: function (request, error) {
            alert ("Beklenmedik bir hata olustu. Hata kodu: 114")
            console.log(error);
        }
    });

}

function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}
  
function drop(ev) {
ev.preventDefault();
var data = ev.dataTransfer.getData("text");
ev.target.appendChild(document.getElementById(data));
}
