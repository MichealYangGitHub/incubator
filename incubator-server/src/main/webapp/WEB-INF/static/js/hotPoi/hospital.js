/**
 * Created by michealyang on 17/3/6.
 */

$("#queryHospital").click(function(){
    var level = $("#hospitalPannel select[name=level]").val();
    HospitalProxy.showHospital(level);
});

/**
 * 添加Hospital按钮响应
 */
$("#addHospital").click(function(){
    var fullName = $.trim($("#addHospitalForm input[name=fullName]").val());
    var alias = $.trim($("#addHospitalForm input[name=alias]").val());
    var location = $.trim($("#addHospitalForm input[name=location]").val());
    var level = $("#addHospitalForm select[name=level]").val();
    var lnglat = $.trim($("#addHospitalForm input[name=lnglat]").val());
    var url = "/hotPoi/hospital/w/add"
    var data = {
        "name": fullName,
        "alias": alias,
        "location": location,
        "level": level,
        "lnglat": lnglat
    };
    var resp = AjaxProxy.queryByPost(url, data);
    if(!resp.success){
        DlgProxy.swalSimpleError(resp.msg);
        return;
    }

    DlgProxy.swalSimpleSuccess(resp.msg);
});

/**
 * 重置添加Hospital 表单
 */
$("#reset").click(function(){
    $("#addHospitalForm input[name=fullName]").val("");
    $("#addHospitalForm input[name=alias]").val("");
    $("#addHospitalForm input[name=location]").val("");
    $("#addHospitalForm input[name=lnglat]").val("");
});


var HospitalProxy = {
    map: undefined,
    initMap: function(){
        this.map = new AMap.Map("container", {
            resizeEnable: true,
            center: [116.418261, 39.921984],
            zoom: 11
        });
        if (!isSupportCanvas()) {
            alert('热力图仅对支持canvas的浏览器适用,您所使用的浏览器不能使用热力图功能,请换个浏览器试试~')
        }
        //判断浏览区是否支持canvas
        function isSupportCanvas() {
            var elem = document.createElement('canvas');
            return !!(elem.getContext && elem.getContext('2d'));
        }

        this.map.plugin(["AMap.Heatmap", "AMap.ToolBar", "AMap.Scale"], function() {
            var toolBar = new AMap.ToolBar();
            var scale = new AMap.Scale();
            this.map.addControl(toolBar);
            this.map.addControl(scale);
        });

        //地图样式： @see http://lbs.amap.com/api/javascript-api/example/personalized-map/set-theme-style
        this.map.setMapStyle("light");


        HospitalProxy.showHospital(0);
    },
    /**
     * 在地图上展示hospital位置及信息
     * @param level  展示类型。0表示全部，1表示一甲，2表示二甲，3表示三甲
     */
    showHospital : function(level) {
        this.map.clearMap();
        if(level == undefined || level == null){
            level = 0
        }
        var url = "/hotPoi/hospital/r/hospitals?level=" + level;
        var resp = AjaxProxy.queryByGet(url);
        if(!resp.success){
            DlgProxy.swalSimpleError(resp.msg);
            return;
        }
        if(resp.data == null || resp.data.length <= 0){
            DlgProxy.swalSimpleWarning("没有任何数据");
            return;
        }
        //开始展示数据
        AMapProxy.addSimpleMarker(this.hospitalToMarker(resp.data), this.map);
        //awesome marker不能自适应，只能通过显示一下Simple marker来保证自适应
        AMapProxy.addAwesomeMarker(this.hospitalToMarker(resp.data), this.map, "medkit", "red", "#fff");
        this.map.setFitView();  //地图自适应
    },

    hospitalToMarker: function(param){
        if(JSBasic._typeof(param) == "array"){
            var ret = [];
            param.forEach(function(ele, i){
                var marker = {
                    title : ele.name,
                    position: ele.lnglat,
                    alias: ele.alias
                }
                ret.push(marker);
            })
            return ret;
        }else if(JSBasic._typeof(param) == "object"){
            var marker = {
                title : param.name,
                position: param.lnglat,
                alias: param.alias
            }
            return marker;
        }
    }
}


