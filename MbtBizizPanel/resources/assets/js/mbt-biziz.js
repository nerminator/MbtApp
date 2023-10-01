$(function () {
    $('.image-editor').cropit({
        allowDragNDrop: false,
        smallImage:'allow'
    });

    $('#savecoverphoto').click(function () {
        // Move cropped image data to hidden input
        var imageData = $('.image-editor').cropit('export');
        $("input[name='imageString']").val(imageData);
        var output = document.getElementById('CoverPhoto');
        output.src = imageData;
        $('#CoverPhotoDiv').show();
        $('#AddCoverPhotoDiv').hide();
    });
});

var j = 5;
if (document.getElementById("startTime_")) {
    var today = moment().format('YYYY-MM-DDT09:00');
    document.getElementById("startTime_").value = today;
}

$("[name='deleteimage']").click(function () {
    $("input[name='imageString']").val('');
    $('#CoverPhotoDiv').hide();
    $('#AddCoverPhotoDiv').show();
    $('#CoverPhoto').attr("src", "");
    $('.cropit-preview-image').attr("src", "");
});

$("[name='deleteimageinedit']").click(function () {

    var id = $('#news_id').val();
    $.ajax({
        url: 'deleteimage',
        type: "POST",
        dataType: "json",
        data: {
            id: id
        },
        success: function (data) {
            if (data == -1) {
                alert("Unexpected error. Please try again later.")
            } else {
                $("input[name='imageString']").val('');
                $('#CoverPhotoDiv').hide();
                $('#AddCoverPhotoDiv').show();
                $('#CoverPhoto').attr("src", "");
                $('.cropit-preview-image').attr("src", "");
            }
        },
        error: function (request, error) {
            console.log(arguments);
        }
    });
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

function getNewsList() {
    $.ajax({
        url: 'getnewslist',
        type: "POST",
        dataType: "json",
        data: {
            pageNumber: 1,
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
                    if (data[a].list_text == null)
                        html += "-";
                    else
                        html += encodeHTML(data[a].list_text);
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

    /*$(document)
        .ajaxStart(function () {
            $('#app').loader('show');
        })
        .ajaxStop(function () {
            $('#app').loader('hide');
        });*/

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
                                    if (data[a].list_text == null)
                                        html += "-";
                                    else
                                        html += encodeHTML(data[a].list_text);
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

    if (document.getElementById("news_id")) {

        if ($("#news_id").val() != "") {
            $.ajax({
                url: 'getnews',
                type: "POST",
                dataType: "json",
                data: {
                    id: $("#news_id").val()
                },
                success: function (data) {
                    if (data == -1) {
                        alert("Unexpected error. Please try again later.")
                    } else {
                        $("[name='listText']").val(data.list_text);
                        $("[name='listTextEn']").val(data.list_text_en);
                        $("[name='title']").val(data.title);
                        $("[name='titleEn']").val(data.title_en);
                        $("[name='text']").val(data.text.substr(0, 10000));
                        $("[name='textEn']").val(data.text_en.substr(0, 10000));
                        $("[name='subTitle']").val(data.sub_title);
                        $("[name='subTitleEn']").val(data.sub_title_en);
                        $("[name='subText']").val(data.sub_text);
                        $("[name='subTextEn']").val(data.sub_text_en);
                        $("[name='url']").val(data.url);
                        $("[name='type']").val(data.type);
                        if ($("[name='type']").val() == 8 || $("[name='type']").val() == 9) {
                            $(".cropit-preview").attr("style", "width: 780px; height: 172px; cursor: move; position: relative;");
                            $("#textDiv").hide();
                        } else {
                            $(".cropit-preview").attr("style", "width: 470px; height: 336px; cursor: move; position: relative;");
                            $("#textDiv").show();
                        }
                        if (data.employee_type != null) {
                            $("[name='employeeType']").val(data.employee_type);
                        }
                        if (data.company_location_id != null) {
                            var numbers = JSON.stringify(data.location_ids).replace(/\"/g, "").split(',');
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

                        if (data.image != null) {
                            $('#CoverPhotoDiv').show();
                            $('#AddCoverPhotoDiv').hide();
                            $('#CoverPhoto').attr("src", data.image)
                        }

                    }
                },
                error: function (request, error) {
                    console.log(arguments);
                }
            });
        }
    }

})
;

var currentRequest = null;
$("[name='searchNews']").keyup(function () {
    $("#allNews").empty();
    j = 5;
    currentRequest = $.ajax({
        url: 'getnewslist',
        type: "POST",
        dataType: "json",
        data: {
            searchText: $("[name='searchNews']").val(),
            pageNumber: 1,
            sortType: $("[name='sortBy']").val()
        },
        beforeSend: function () {
            if (currentRequest != null) {
                currentRequest.abort();
            }
        },
        success: function (data) {
            if (data == -1) {
                alert("Aramanızda harf sayı . ve - dışında bir karakter kullanmayınız.")
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
                    if (data[a].list_text == null)
                        html += "-";
                    else
                        html += encodeHTML(data[a].list_text);
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
        }
    });
});

$("#allNews").on("click", ".remove", (function () {
    var id = parseInt($(this).attr("data"));
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
    } else {
        $("[name='discountTypeDiv']").append("<label class='col-md-12 translation_title' for='discountType'>DISCOUNT TYPE</label> <select name='discountType' class='col-md-12 form-control' style='height: 45px;'> <option value='2'>Education and Personel Development</option> <option value='3'>Event and Entertainment</option> <option value='4'>Home and Life</option> <option value='5'>Gastronomy and Food</option> <option value='6'>Real Estate</option> <option value='7'>Clothing and Fashion</option> <option value='8'>Automotive</option> <option value='9'>Health and Sports</option> <option value='12'>Tourism and Car Rental</option> <option value='14'>Technology and Telecom</option> <option value='13'>Other</option> </select>");
    }
    if ($("[name='type']").val() == 8 || $("[name='type']").val() == 9) {
        $("#phoneNumber").hide();
        $(".cropit-preview").attr("style", "width: 780px; height: 172px; cursor: move; position: relative;");
        $("#textDiv").hide();
        if ($("[name='type']").val() == 9)
            $("#phoneNumber").show();
    } else {
        $(".cropit-preview").attr("style", "width: 470px; height: 336px; cursor: move; position: relative;");
        $("#textDiv").show();
        $("#phoneNumber").hide();
    }
    /*
    if ($("[name='type']").val() == 8 || $("[name='type']").val() == 9 || $("[name='type']").val() == 3)
        $("#AddCoverPhotoDiv").show();
    else
        $("#AddCoverPhotoDiv").hide();
    */
}