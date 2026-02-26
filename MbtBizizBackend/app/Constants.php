<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 3.06.2018
 * Time: 23:10
 */

namespace App;


class Constants
{
    const
        COMPANY_LOCATION_GENEL_MUDURLUK = 1,
        COMPANY_LOCATION_HOSDERE = 2,
        COMPANY_LOCATION_AKSARAY = 3;

    const
        SHUTTLE_TYPE_EMPLOYEE = 1,
        SHUTTLE_TYPE_RING = 2,
        SHUTTLE_TYPE_OTHER = 3,
        SHUTTLE_TYPE_INTERLOCATION = 4;

    const
        SHUTTLE_TIME_TYPE_MORNING = 1,
        SHUTTLE_TIME_TYPE_NOON = 2,
        SHUTTLE_TIME_TYPE_EVENING = 3,
        SHUTTLE_TIME_TYPE_NIGHT = 4;

    const
        SHUTTLE_TIME_TYPE_MORNING_NAME = "Sabah",
        SHUTTLE_TIME_TYPE_NOON_NAME = "Öğle",
        SHUTTLE_TIME_TYPE_EVENING_NAME = "Akşam",
        SHUTTLE_TIME_TYPE_NIGHT_NAME = "Gece";

    const
        EMPLOYEE_TYPE_WHITE_COLLAR = 1,
        EMPLOYEE_TYPE_BLUE_COLLAR = 2;
}