<h1>Welcome Incubator！</h1>


<#if user??>
    <p>ID：${user.id}</p>
    <p>用户名：${user.userName}</p>
</#if>

<p><a href="${ssoLogoutUrl!'#'}">注销</a></p>

<ul>
    <li><a href="/houseSpy/lianjia/r/index">链家数据</a></li>
    <li><a href="/hotPoi/hospital/r/index">Hot POI</a></li>
</ul>
