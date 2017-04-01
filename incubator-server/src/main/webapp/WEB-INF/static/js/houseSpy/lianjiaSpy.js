/**
 * Created by michealyang on 17/3/6.
 */


//点击显示addModal
$("#fix-add").click(function(){
    var $addModal = $("#addModal");
    $("#addModal input[name=url]").val("");
    $("#addModal input[name=url]").focus();
    $addModal.modal("show");
})

$("body").on("click", "#emptyDate a", function(){
    var $addModal = $("#addModal");
    $("#addModal input[name=url]").val("");
    $("#addModal input[name=url]").focus();
    $addModal.modal("show");
})

//监听addModal显示，bind回车响应
$("body").on('shown.bs.modal', "#addModal", function (e) {
    KeyboardUtil.bindEnter($("#addModal button[name=addModal-confirm]"));
});

//监听addModal显示，unbind回车响应
$("body").on('hidden.bs.modal', "#addModal", function (e) {
    KeyboardUtil.unbindEnter();
});

//监听搜索框被focus时，bind回车响应
$("body").on("focus", "#nav-search input[name=search]", function(e){
    KeyboardUtil.bindEnter($("#nav-search #nav-search-btn"))
})

//搜索框不被focus时，unbind回车响应
$("body").on("blur", "#nav-search input[name=search]", function(e){
    KeyboardUtil.unbindEnter();
})

//搜索按钮
$("#nav-search #nav-search-btn").click(function(){
    var content = $.trim($("#nav-search input[name=search]").val());
    LJHouseSpyProxy.getHouseInfo(content);
});


//addModal确认按钮响应
$("#addModal button[name=addModal-confirm]").click(function(){
    var url = $.trim($("#addModal input[name=url]").val());
    $("#loading").removeClass('hidden');
    var wait = window.setTimeout(function(){
        $("#loading").addClass('hidden');
        DlgProxy.swalSimpleError("响应超时");
    }, 10000)
    var queryUrl = "/houseSpy/lianjia/w/addSpy";
    var resp = AjaxProxy.queryByPost(queryUrl, {"url": url});
    if(!resp.success){
        DlgProxy.swalSimpleError(resp.msg);
        $("#loading").addClass('hidden');
        window.clearTimeout(wait);
        return;
    }
    DlgProxy.swalSimpleSuccess(resp.msg);
    $("#loading").addClass('hidden');
    window.clearTimeout(wait);
    window.setTimeout(function(){
        window.location.reload();
    }, 1000)
});



var LJHouseSpyProxy = {
    showAll: false,  //一个全局的flag
    getHouseInfo : function(community){
        var url = "/houseSpy/lianjia/r/list?community=" + community;
        if(community == ""){
            if(this.showAll){
                return;
            }else{
                this.showAll = true;
            }
        }else{
            this.showAll = false;
        }

        var resp = AjaxProxy.queryByGet(url);
        if(!resp.success){
            DlgProxy.swalSimpleError(resp.msg);
            return;
        }
        var houseInfos = resp.data;
        if(JSBasic._typeof(houseInfos) != "array"){
            DlgProxy.swalSimpleError("数据格式错误");
            return;
        }

        if(houseInfos.length == 0){
            this.showEmptyInfo();
            return;
        }

        this.showHouseInfo(houseInfos);

        //DlgProxy.swalSimpleSuccess("有" + houseInfos.length + "条数据");
        //$houseInfo = $("#houseInfos");
        //$houseInfo.html("")
        //if(houseInfos.length <= 0){
        //    $houseInfo.html("没有任何数据")
        //}else{
        //    $houseInfo.html("有" + houseInfos.length + "条数据");
        //}

    },

    showHouseInfo : function(data){
        var viewTemplate = '\
        <div class="row houseInfo" style=" border: 1px solid #ddd; margin-top:40px; border-radius: 15px"> \
            <div class="row"> \
                <div class="col-md-4"> \
                    <img class="img-thumbnail col-md-12" src="LJ_IMG"> \
                </div> \
                <div class="col-md-8"> \
                    <div class="col-md-12"><h3><a href="LJ_URL" target="_blank">LJ_TITLE</a></h3></div> \
                    <div class="rows" style="color: gray; height: 100px;"> \
                        <div class="col-md-5 col-sm-5 col-xs-5"> \
                            <span>总价：</span><span class="ft-red"><b>LJ_TOTAL万元</b></span> \
                        </div> \
                        <div class="col-md-5 col-sm-5 col-xs-5"> \
                            <span>面积：</span><span>LJ_AREA㎡</span> \
                        </div> \
                        <div class="col-md-5 col-sm-5 col-xs-5"> \
                            <span>单价：</span><span>LJ_PRICE元/平</span> \
                        </div> \
                        <div class="col-md-5 col-sm-5 col-xs-5"> \
                            <span>房型：</span><span>LJ_HOUSE_TYPE</span> \
                        </div> \
                        <div class="col-md-5 col-sm-5 col-xs-5"> \
                            <span>小区：</span><span>LJ_COMMUNITY</span> \
                        </div> \
                        <div class="col-md-5 col-sm-5 col-xs-5"> \
                            <span>年份：</span><span>LJ_BUILT_YEAR年</span> \
                        </div> \
                    </div> \
                </div> \
            </div> \
            <div class="col-md-12"> \
                <div id="CHART_ID" ></div> \
            </div> \
        </div>';
        //<canvas id="CHART_ID" style="width: 150px" width="100" height="30"></canvas> \


        var $houseInfos = $("#houseInfos");
        //console.log(data);
        $houseInfos.html("");

        var imgUrl = "http://pic.58pic.com/58pic/13/69/50/82n58PICWdQ_1024.jpg";

        data.forEach(function(e, i){
            var ljHouse = e.ljHouse;
            var ljHouseTraces = e.ljHouseTraces;
            var lastTrace = LJHouseSpyHelper.getLastTrace(ljHouseTraces);
            var view = viewTemplate.replace("LJ_IMG", imgUrl);
            var chartId = "Chart" + i;
            view = view.replace("LJ_URL", ljHouse.url);
            view = view.replace("LJ_TITLE", ljHouse.title);
            view = view.replace("LJ_TOTAL", lastTrace == null ? 0 : lastTrace.total);
            view = view.replace("LJ_AREA", ljHouse.area);
            view = view.replace("LJ_PRICE", lastTrace == null ? 0.0 : lastTrace.unitPrice);
            view = view.replace("LJ_HOUSE_TYPE", ljHouse.houseType);
            view = view.replace("LJ_COMMUNITY", ljHouse.community);
            view = view.replace("LJ_BUILT_YEAR", ljHouse.builtYear);
            view = view.replace("CHART_ID", chartId);

            $houseInfos.append(view);

            LJHouseSpyProxy.showTraces(chartId, ljHouseTraces, e.timeSpan);
        })
    },

    showEmptyInfo: function(){
        var $houseInfos = $("#houseInfos");
        $houseInfos.html("");
        var template = '\
        <div id="emptyDate"> \
            <div class="text-center mg-top-10vh"> \
                <img src="http://onice2szs.bkt.clouddn.com/illust-404.png"> \
            </div> \
            <div class="text-center mg-top-5vh ft-size-20">没有想要的数据？<span><a href="javascript:void(0)">快来添加监控吧>></a></span></div> \
        </div>';
        $houseInfos.append(template);
    },

    showTraces : function(chartId, traces, timeSpan){
        if(traces == null || traces.length == 0) return;
        var ctx = document.getElementById(chartId);
        var data = [];
        traces.forEach(function(e, i){
            if(e == null){
                data.push(0);
            }else{
                data.push(e.total);
            }
        })

        //console.log(timeSpan);

        Highcharts.chart(chartId, {

            title: {
                text: '历史价格'
            },

            subtitle: {
                text: ''
            },

            colors: ['orange'],

            xAxis: {    //x轴
                categories: timeSpan
            },

            yAxis: {    //y轴
                title: {
                    text: '总价/万元'
                }
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle'
            },

            plotOptions: {
                series: {
                }
            },

            series: [{
                //type: 'area',
                name: '单位：万元',
                data: data
            }]

        });

        //var myChart = new Chart(ctx, {
        //    type: 'line',
        //    data: {
        //        labels: timeSpan,
        //        datasets: [{
        //            label: '历史总价/万元',
        //            data: data,
        //            backgroundColor: [
        //                'rgba(255, 99, 132, 0.2)',
        //                'rgba(54, 162, 235, 0.2)',
        //                'rgba(255, 206, 86, 0.2)',
        //                'rgba(75, 192, 192, 0.2)',
        //                'rgba(153, 102, 255, 0.2)',
        //                'rgba(255, 159, 64, 0.2)'
        //            ],
        //            borderColor: [
        //                'rgba(255,99,132,1)',
        //                'rgba(54, 162, 235, 1)',
        //                'rgba(255, 206, 86, 1)',
        //                'rgba(75, 192, 192, 1)',
        //                'rgba(153, 102, 255, 1)',
        //                'rgba(255, 159, 64, 1)'
        //            ],
        //            borderWidth: 1
        //        }]
        //    },
        //    options: {
        //        scales: {
        //            yAxes: [{
        //                ticks: {
        //                    beginAtZero: true
        //                }
        //            }]
        //        }
        //    }
        //})
    }
}

var LJHouseSpyHelper = {
    getLastTrace: function(traces){
        if(undefined == traces || JSBasic._typeof(traces) != "array"){
            return null;
        }
        for(var i=traces.length - 1; i>=0; i--){
            if(traces[i] != null) return traces[i];
        }
        return null;
    }
}



