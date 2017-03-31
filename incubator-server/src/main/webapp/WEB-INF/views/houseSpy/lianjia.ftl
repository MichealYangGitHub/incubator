<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="http://cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>

    <link href="http://cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">

    <!-- Animate CSS -->
    <link href="http://cdn.bootcss.com/animate.css/3.5.2/animate.min.css" rel="stylesheet">

    <#--Sweet Alert-->
    <link href="http://cdn.bootcss.com/sweetalert/1.1.3/sweetalert.min.css" rel="stylesheet">

    <#--ChartJs-->
    <#--<script src="http://cdn.bootcss.com/Chart.js/2.5.0/Chart.min.js"></script>-->
    <#--<script src="http://cdn.bootcss.com/Chart.js/2.5.0/Chart.bundle.min.js"></script>-->
    <script src="http://cdn.bootcss.com/highcharts/5.0.9/highcharts.js"></script>
    <script src="http://cdn.bootcss.com/highcharts/5.0.9/js/modules/exporting.js"></script>

    <link href="http://cdn.bootcss.com/loaders.css/0.1.2/loaders.min.css" rel="stylesheet">
    <script src="http://cdn.bootcss.com/loaders.css/0.1.2/loaders.css.min.js"></script>

    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="http://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="http://cdn.bootcss.com/sweetalert/1.1.3/sweetalert.min.js"></script>

    
    <title></title>
</head>
<style type="text/css">
    html, body{
        height: 100%;
    }
    .houseInfo-hover{
        box-shadow:0 0 12px rgba(26,239,239,1);
        -moz-box-shadow:0 0 12px rgba(26,239,239,1);
        -webkit-box-shadow:0 0 12px rgba(26,239,239,1);
    }

    #main {
        height: 400px;
        margin: 0 auto
    }
    
    .full{
        position: fixed;
        background-color: #aaa;
        width: 100vw;
        height: 100vh;
        top:0;
        bottom: 0;
        right: 0;
        left: 0;
        opacity: 0.8;
        text-align: center;
        line-height: 100vh;
        vertical-align: middle;
        z-index: 99999;
    }
    .full>div{
        background-color: red;
        margin-top: 50vh;
        margin-left: 50vw;
    }
</style>
<body>
    <nav id="indexNav" class="navbar navbar-inverse">
        <input id="navType" type="text" value="${type!''}" class="hidden">
        <input id="navUrl" type="text" value="${url!''}" class="hidden">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#realNav" aria-expanded="true">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/houseSpy/lianjia/r/index">HouseSpy</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="navbar-collapse collapse in" id="realNav" aria-expanded="true">
                <ul class="nav navbar-nav">
                    <li name="nav-allHouses" class="active"><a href="javascript:void(0)">全部<span class="sr-only">(current)</span></a></li>
                    <li name="nav-myConcern" ><a href="javascript:void(0)">我的关注</a></li>
                </ul>
                <div class="navbar-form navbar-left hidden-xs" role="search" action="javascript:void(0);">
                    <div class="input-group" style="width: 520px">
                        <input type="text" name="search" class="form-control" placeholder="搜索房源" tabindex="3">
                    <span class="input-group-btn">
                        <button id="nav-search" class="btn btn-default btn-icon" type="submit"><i class="fa fa-search"></i></button>
                    </span>

                    </div>
                </div>

                <ul class="nav navbar-nav navbar-right">
                <#if user??>
                    <li name="login"><a href="javascript:void(0)">${user.name!'NULL'}</a></li>
                    <li name="logout"><a href="javascript:void(0)">退出</a></li>
                <#else>
                    <li name="login"><a href="/user/r/login">登录</a></li>
                    <li name="signup"><a href="/user/r/signup">注册</a></li>
                </#if>

                </ul>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>
    <div class="navbar-default navbar visible-xs hidden-print">
        <form class="js-search-form" role="search" action="javascript:void(0);">
            <div class="input-group" style="padding: 15px;">
                <input id="nav-search" type="text" name="search" class="form-control" placeholder="搜索房源" tabindex="3">
      <span class="input-group-btn">
        <button class="btn btn-default btn-icon" type="submit"><i class="fa fa-search"></i></button>
      </span>
            </div>
        </form>
    </div>
    <div class="container">
        <div id="houseInfos">
        </div>
        <#--<div id="main" class="col-md-12"></div>-->
    </div>

    <div>
        <button id="fix-add" class="btn btn-default" style="position: fixed; bottom: 20px; right: 10px;">
            没找到？<br>点击添加
        </button>
    </div>


    <div id="addModal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">添加要监控的房源</h4>
                </div>
                <div class="modal-body">
                    <p class="text-muted bg-danger" style="height: 40px; padding: 10px">您想关注的房源不在监控中？快快添加监控吧</p>
                    <div class="form-group">
                        <label for="exampleInputEmail1">房源链接</label>
                        <input type="text" class="form-control" name="url" placeholder="房源在链家的URL">
                    </div>
                    <div style="padding: 10px;">
                        <p class="text-muted"><i>
                            1. 复制房源在浏览器中的地址，粘贴到上面的输入框中，点击确定，即可添加监控
                        </i></p>
                        <p class="text-muted"><i>2. 链接示例:</i></p>
                        <p class="text-muted"><i>&nbsp;&nbsp;&nbsp;&nbsp;移动版 - https://m.lianjia.com/bj/ershoufang/101100954046.html</i></p>
                        <p class="text-muted"><i>&nbsp;&nbsp;&nbsp;&nbsp;Web版 - https://bj.lianjia.com/ershoufang/101100954046.html</i></p>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" name="addModal-confirm" class="btn btn-success">确定</button>
                    <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <div id="loading" class="loader-inner ball-spin-fade-loader full hidden"></div>
</body>
</html>
<script type="text/javascript" src="/static/js/util.js"></script>
<script type="text/javascript" src="/static/js//houseSpy/lianjiaSpy.js"></script>
<link href="/static/css/base.css" rel="stylesheet">
<script type="application/javascript">
    $(document).ready(function(){
        LJHouseSpyProxy.getHouseInfo();
    });

    $(".houseInfo").hover(function(){
        $(this).addClass("houseInfo-hover");
    },function(){
        $(this).removeClass("houseInfo-hover");
    });
//
//    function showImg( url ) {
//        var frameid = 'frameimg' + Math.random();
//        window.img = '<img id="img" src=\''+url+'?'+Math.random()+'\' /><script>window.onload = function() { parent.document.getElementById(\''+frameid+'\').height = document.getElementById(\'img\').height+\'px\'; }<'+'/script>';
//        document.write('<iframe id="'+frameid+'" src="javascript:parent.img;" frameBorder="0" scrolling="no" width="100%"></iframe>');
//    }
//    showImg('https://image1.ljcdn.com/110000-inspection/6ab4a2da-0c43-425c-b601-738ecde1fa69.jpg.500x280.jpg');
</script>