<h1>Welcome Incubator！</h1>


<#if user??>
    <p>ID：${user.id!0}</p>
    <p>用户名：${user.name!"NULL"}</p>
<p><a href="${ssoLogoutUrl!'/auth/user/r/logout'}">注销</a></p>
<#else >
<p><a href="${'/auth/user/r/login'}">登录</a></p>
<p><a href="${'/auth/user/r/signup'}">注册</a></p>
</#if>



<ul>
    <li><a href="/houseSpy/lianjia/r/index">链家数据</a></li>
    <li><a href="/hotPoi/hospital/r/index">Hot POI</a></li>
    <li><a href="/webapp/appstore/r/index">Web App</a></li>
</ul>
