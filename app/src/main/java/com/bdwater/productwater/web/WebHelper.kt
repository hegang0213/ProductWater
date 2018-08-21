package com.bdwater.productwater.web

import com.google.gson.Gson

/**
 * Created by hegang on 2018/8/7.
 */
class WebHelper {
    companion object Url {
        const val base = "http://222.222.178.213:1111/AssetManagementService/"
        object Asset {
            var url = Url.base + "WebService.asmx"
            object Methods {
                const val allUser = "AllUser"
                const val login = "Login"
                const val getDevices = "GetDeviceListOrderByDate"
                const val getDevice = "GetDeviceByID"
                const val getServices = "GetServicesByDeviceID"
                const val getDevicesBySearch = "GetDeviceListBySearch"
            }
        }
        object Data {
            var url = Url.base + "DataWebService.asmx"
            object Methods {
                const val getAll = "GetAll";
            }
        }
        fun toJsonResult(jsonString:String): JsonResult {
            return Gson().fromJson(jsonString, WebHelper.JsonResult::class.java);
        }
    }
    object WebService{
        object Asset {
            fun allUser(): CustomPostString {
                return CustomPostString(Url.Asset.url, Url.Asset.Methods.allUser);
            }
            fun login():CustomPostString {
                return CustomPostString(Url.Asset.url, Url.Asset.Methods.login)
            }
            fun getDevices():CustomPostString {
                return CustomPostString(Url.Asset.url, Url.Asset.Methods.getDevices)
            }
            fun getDevice():CustomPostString {
                return CustomPostString(Url.Asset.url, Url.Asset.Methods.getDevice)
            }
            fun getServices(): CustomPostString {
                return CustomPostString(Url.Asset.url, Url.Asset.Methods.getServices)
            }
            fun getDevicesBySearch() : CustomPostString {
                return CustomPostString(Url.Asset.url, Url.Asset.Methods.getDevicesBySearch)
            }
        }
        object Data {
            fun getAll(): CustomPostString {
                return CustomPostString(Url.Data.url, Url.Data.Methods.getAll);
            }
        }
    }


    class JsonResult {
        var code: Int = 0;
        var message: String = "";
        var data: String = "";
        var stackTrace: String = "";
    }
}