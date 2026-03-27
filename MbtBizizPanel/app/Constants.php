<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 21.06.2018
 * Time: 22:35
 */

namespace App;


class Constants
{
    const
        SORT_TYPE_CREATED_AT = 1,
        SORT_TYPE_START_DATE = 2;

    const
        STATUS_DELETED = 0,
        STATUS_ACTIVE = 1,
        STATUS_PASSIVE = 2;

    const
        EMPLOYEE_TYPE_ALL = 0,
        EMPLOYEE_TYPE_WHITE_COLLAR = 1,
        EMPLOYEE_TYPE_BLUE_COLLAR = 2;

    const
        NEWS_TYPE_ACTIVITY = 2,
        NEWS_TYPE_DISCOUNT = 3,
        NEWS_TYPE_LEAVE = 4,
        NEWS_TYPE_PASSING = 5;

    const
        APN_CERTIFICATE_PATH = "/app/MbtApp.pem";
}
