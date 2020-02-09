Power = {
    version: "1.0",
    rootPath: "",
    contextPath: "",
    jsPath: "/static/js/",
    cssPath: "/static/css/",
    imgPath: "/static/img/",
    filtPath: ""
}

Power.initIframe = function (B, A) {
    this.rootPath = B + "/";
    this.contextPath = A + "/";
    this.jsPath = B + "/static/js/";
    this.cssPath = B + "/static/css/";
    this.imgPath = B + "/static/img/"
};
$(function () {
    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a,
            function () {
                if (o[this.name]) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]]
                    }
                    o[this.name].push(this.value || "")
                } else {//无新加
                    o[this.name] = this.value || ""
                }
            });
        return o
    };
})