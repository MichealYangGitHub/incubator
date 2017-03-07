<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=88907251c6b5e78a755f6d0600f8b810"></script>
    <script src="//webapi.amap.com/ui/1.0/main.js"></script>

    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="http://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>

    <link href="http://cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">

    <!-- Animate CSS -->
    <link href="http://cdn.bootcss.com/animate.css/3.5.2/animate.min.css" rel="stylesheet">

<#--Sweet Alert-->
    <link href="http://cdn.bootcss.com/sweetalert/1.1.3/sweetalert.min.css" rel="stylesheet">

    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="http://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="http://cdn.bootcss.com/sweetalert/1.1.3/sweetalert.min.js"></script>


    <title></title>
</head>
<style type="text/css">
    html, body{
        height: 100%;
    }

    #container {
        width:100%;
        height: 90%;
    }
</style>
<body>
    <div id="container"></div>
    <div class="form-group" id="hospitalPannel">
        <div class="row">
            <div class="col-sm-5">
                <select class="form-control" name="level">
                    <option value="0">不限</option>
                    <option value="1">一级甲等</option>
                    <option value="2">二级甲等</option>
                    <option value="3">三级甲等</option>
                </select>
            </div>
            <div class="col-sm-5">
                <button class="btn btn-success col-sm-4 col-xs-12" id="queryHospital">查询</button>
            </div>
        </div>
    </div>
</body>
</html>
<script type="text/javascript" src="/static/js/util.js"></script>
<script type="text/javascript" src="/static/js/hotPoi/hospital.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        HospitalProxy.initMap();

    });
</script>