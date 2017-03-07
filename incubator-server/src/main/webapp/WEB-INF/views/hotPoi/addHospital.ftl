<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=88907251c6b5e78a755f6d0600f8b810"></script>

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
</style>
<body>
<div id="main" class="container">
    <form class="form-horizontal" id="addHospitalForm" style="margin-top: 50px;">
        <div class="form-group">
            <label class="col-sm-2 control-label">全称</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="fullName" placeholder="全称">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">简称</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="alias" placeholder="简称">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">地址</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="location" placeholder="地址">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">等级</label>
            <div class="col-sm-10">
                <select class="form-control" name="level">
                    <option value="1">一级甲等</option>
                    <option value="2">二级甲等</option>
                    <option value="3">三级甲等</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">经纬度</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="lnglat" placeholder="经纬度,如 116.397428, 39.90923">
            </div>
        </div>
        <div class="form-group">
            <div class="row col-sm-offset-2 ">
                <div class="col-sm-10">
                    <button type="button" id="addHospital" class="btn btn-default">提交</button>
                    <button type="button" id="reset" class="btn btn-default col-sm-offset-1">重置</button>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
<script type="text/javascript" src="/static/js/util.js"></script>
<script type="text/javascript" src="/static/js/hotPoi/hospital.js"></script>