package com.bdwater.productwater.web

import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import okhttp3.MediaType
import java.lang.Exception

/**
 * Created by hegang on 2018/8/7.
 */
class CustomPostString(u:String, mn: String) {
    var url:String
    var methodName :String
    var parameters: HashMap<String, String> = HashMap<String, String>();
    val soapFormat = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <%s xmlns=\"http://tempuri.org/\" >\n" +
            "       %s" +
            "    </%s>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>"
    val parameterFormat = "<%s>%s</%s>\n"
    init {
        this.url = u;
        this.methodName = mn;
    }

    fun addParameter(key: String, value: String): CustomPostString
    {
        this.parameters.put(key, value);
        return this;
    }
    private fun buildSoapBody() : String {
        var parameterString = "";
        if(this.parameters.size > 0) {
            for (key: String in this.parameters.keys) {
                parameterString += this.parameterFormat.format(key, this.parameters.get(key), key);
            }
        }
        var soapBody = this.soapFormat.format(this.methodName,
                parameterString,
                this.methodName);
        return soapBody;
    }
    fun execute(callback: GeneralCallback) {
        var methodName = this.methodName;
        OkHttpUtils
                .postString()
                .url(this.url)
                .mediaType(MediaType.parse("text/xml"))
                .content(buildSoapBody())
                .tag(this.methodName)
                .build()
                .execute(object : StringCallback() {
                    override fun onError(call: Call?, e: Exception?, id: Int) {
                        callback.onError(call, e, id);
                        callback.onFinally()
                    }

                    override fun onResponse(response: String?, id: Int) {
                        var jsonString = response!!;
                        var tagStart = "<" + methodName + "Result>";
                        var tagEnd = "</" + methodName + "Result>";
                        var start = jsonString.indexOf(tagStart);
                        if(start > 0) {
                            var end = jsonString.indexOf(tagEnd);
                            jsonString = jsonString.substring(start + tagStart.length, end);
                            var result = WebHelper.toJsonResult(jsonString)
                            callback.onResponse(response, result, id);
                            callback.onFinally()
                            return;
                        }
                        callback.onLocalError(jsonString, Exception("Bad json format string."))
                        callback.onFinally()
                    }
                })

    }
}