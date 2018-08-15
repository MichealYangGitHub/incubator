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

<#--分页-->
    <script src="http://cdn.bootcss.com/twbs-pagination/1.4.1/jquery.twbsPagination.min.js"></script>


    <title></title>
</head>
<style type="text/css">
    html, body{
        height: 100%;
    }
    #menus a{
        color: #FFFFFF;
    }
    #menus .panel-heading{
        background-color: #3c3b3b;
    }
    #menus .panel-default{
        background-color: #3c3b3b;
    }
    #menus .panel-default li{
        color: #FFFFFF;
    }
</style>
<body>
<nav id="indexNav" class="navbar navbar-inverse" style="z-index: 999">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#realNav" aria-expanded="true">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">WebApp</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="navbar-collapse collapse in" id="realNav" aria-expanded="true">
            <ul class="nav navbar-nav">
                <li name="nav-allHouses" ><a href="/webapp/self/r/index">我的收藏<span class="sr-only">(current)</span></a></li>
                <li name="nav-myConcern" class="active"><a href="javascript:void(0)">工具箱</a></li>
            </ul>
            <div id="nav-search" class="navbar-form navbar-left hidden-xs" role="search" action="javascript:void(0);">
                <div class="input-group" style="width: 520px">
                    <input type="text" name="search" class="form-control" placeholder="搜索工具" tabindex="3">
                    <span class="input-group-btn">
                        <button id="nav-search-btn" class="btn btn-default btn-icon" type="submit"><i class="fa fa-search"></i></button>
                    </span>

                </div>
            </div>

            <ul class="nav navbar-nav navbar-right">
            <#if user??>
                <li name="login"><a href="javascript:void(0)">${user.name!'NULL'}</a></li>
                <li name="logout"><a href="/auth/user/r/logout">退出</a></li>
            <#else>
                <li name="login"><a href="/auth/user/r/login">登录</a></li>
                <li name="signup"><a href="/auth/user/r/signup">注册</a></li>
            </#if>

            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
<div id="nav-search" class="navbar-default navbar visible-xs hidden-print">
    <form class="js-search-form" role="search" action="javascript:void(0);">
        <div class="input-group" style="padding: 15px;">
            <input id="nav-search-btn" type="text" name="search" class="form-control" placeholder="搜索工具" tabindex="3">
                <span class="input-group-btn">
                    <button class="btn btn-default btn-icon" type="submit"><i class="fa fa-search"></i></button>
                </span>
        </div>
    </form>
</div>
<div class="container">
    <div id="webapps">
        <div id="menus" style="height: 100%;width: 250px; background-color: #2f2f2f; position: fixed; left: 0; top:50px; padding-top: 30px">
            <div id="category-panel">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <div class="panel panel-default"><!--这是每个pannel的开始-->
                        <div class="panel-heading" role="tab" ">
                            <a role="button" data-toggle="collapse" data-parent="#accordion" href="#" aria-expanded="true"
                               aria-controls="collapseOne"><!--指定响应id为collapseOne-->
                                <h4 class="panel-title">最多收藏</h4>
                            </a>
                        </div>
                    </div><!--panel end-->
                    <#list 1..3 as menu>
                        <div class="panel panel-default"><!--这是每个pannel的开始-->
                            <div class="panel-heading" role="tab" id="Heading_${menu}">
                                <a role="button" data-toggle="collapse" data-parent="#accordion" href="#Collapse_${menu}" aria-expanded="true"
                                   aria-controls="collapseOne"><!--指定响应id为collapseOne-->
                                    <h4 class="panel-title">${menu}</h4>
                                </a>
                            </div>
                            <div id="Collapse_${menu}" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="Heading_${menu}"><!--这个id
                            是collapseOne，collapse组件即是通过id来定位collapse pannel的-->
                                <div class="panel-body">
                                    <ul>
                                        <#list 1..4 as sub>
                                            <li class="category-li <#if categoryId?? && categoryId=subCategory.id>category-active</#if>"
                                                data-id="${sub}">${sub}</li>
                                        </#list>
                                    </ul>
                                </div>
                            </div>
                        </div><!--panel end-->
                    </#list>
                <#if categoryList?? && categoryList?size gt 0>
                    <#list categoryList as category>
                        <div class="panel panel-default"><!--这是每个pannel的开始-->
                            <div class="panel-heading" role="tab" id="Heading_${category.id}">
                                <a role="button" data-toggle="collapse" data-parent="#accordion" href="#Collapse_${category.id}" aria-expanded="true" aria-controls="collapseOne"><!--指定响应id为collapseOne-->
                                    <h4 class="panel-title">${category.name!"NULL"}</h4>
                                </a>
                            </div>
                            <div id="Collapse_${category.id}" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="Heading_${category.id}"><!--这个id 是collapseOne，collapse组件即是通过id来定位collapse pannel的-->
                                <div class="panel-body">
                                    <ul>
                                        <#if category.subCategoryList?? && category.subCategoryList?size gt 0>
                                            <#assign  subCategoryList = category.subCategoryList>
                                            <#list subCategoryList as subCategory>
                                                <li class="category-li <#if categoryId?? && categoryId=subCategory.id>category-active</#if>" data-id="${subCategory.id}">${subCategory.name!"NULL"}</li>
                                            </#list>
                                        </#if>
                                    </ul>
                                </div>
                            </div>
                        </div><!--panel end-->
                    </#list>
                </#if>
                </div>
            </div>
        </div>
        <div id="panel"></div>
    </div>
<#--<div id="main" class="col-md-12"></div>-->
</div>



<#--<div id="addModal" class="modal fade" tabindex="-1" role="dialog">-->
    <#--<div class="modal-dialog" role="document">-->
        <#--<div class="modal-content">-->
            <#--<div class="modal-header">-->
                <#--<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>-->
                <#--<h4 class="modal-title">添加要监控的房源</h4>-->
            <#--</div>-->
            <#--<div class="modal-body">-->
                <#--<p class="text-muted bg-danger" style="height: 40px; padding: 10px">您想关注的房源不在监控中？快快添加监控吧</p>-->
                <#--<div class="form-group">-->
                    <#--<label for="exampleInputEmail1">房源链接</label>-->
                    <#--<input type="text" class="form-control" name="url" placeholder="房源在链家的URL">-->
                <#--</div>-->
                <#--<div style="padding: 10px;">-->
                    <#--<p class="text-muted"><i>-->
                        <#--1. 复制房源在浏览器中的地址，粘贴到上面的输入框中，点击确定，即可添加监控-->
                    <#--</i></p>-->
                    <#--<p class="text-muted"><i>2. 链接示例:</i></p>-->
                    <#--<p class="text-muted"><i>&nbsp;&nbsp;&nbsp;&nbsp;移动版 - https://m.lianjia.com/bj/ershoufang/101100954046.html</i></p>-->
                    <#--<p class="text-muted"><i>&nbsp;&nbsp;&nbsp;&nbsp;Web版 - https://bj.lianjia.com/ershoufang/101100954046.html</i></p>-->
                <#--</div>-->
            <#--</div>-->
            <#--<div class="modal-footer">-->
                <#--<button type="button" name="addModal-confirm" class="btn btn-success">确定</button>-->
                <#--<button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>-->
            <#--</div>-->
        <#--</div><!-- /.modal-content &ndash;&gt;-->
    <#--</div><!-- /.modal-dialog &ndash;&gt;-->
<#--</div><!-- /.modal &ndash;&gt;-->

<div id="loading" class="loader-inner ball-spin-fade-loader full hidden"></div>


<div class="text-center mg-top-5vh">
    <ul id="pagination" class="pagination"></ul>
</div>


</body>
</html>
<script type="text/javascript" src="/static/js/util.js"></script>
<script type="text/javascript" src="/static/js/webapp/appstore.js"></script>
<link href="/static/css/base.css" rel="stylesheet">
<script type="application/javascript">
    $(document).ready(function(){
    });
</script>