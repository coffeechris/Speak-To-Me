<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://www.w3.org/TR/xhtml1/strict"
                xmlns:xlink="http://www.w3.org/1999/xlink"> 

<xsl:strip-space elements="article body p section normallist"/>
<xsl:output
   method="xml"
   indent="yes"
   encoding="iso-8859-1"
/>

<xsl:template match="article">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
<head>
    <title><xsl:value-of select="name"/></title>
	<link rel="stylesheet" type="text/css" media="screen" href="style.css" />
    <script type="text/javascript" src="javascript/tooltip.js"></script>
<script language="JavaScript">
function outsideLink() {

    alert ("External link disabled.");

}

function toggleBookbag(docID, imageNum) {

    document.getElementById("bookbagImg"+imageNum).src = "changeSmallBookbagStatus.php?id="+docID+"#"+Math.random();
    setTimeout("refreshBookbag()",100);

}

function refreshBookbag() {

    top.frames['bookbag'].location.href = "viewBookbag.php";
    
}

function loadArticle(article) {

    top.frames['article'].location.href = "followLinkToPage.php?id="+article;
    setTimeout("refreshHistory('"+article+"')",100);

}

function refreshHistory(article) {

    top.frames['history'].location.href = "viewHistory.php";
    top.frames['navigation'].location.href = "navigation.php?id="+article;
    
}

</script>
</head>
   <xsl:apply-templates/>
 </html>
</xsl:template>

<xsl:template match="article/name">
</xsl:template>

<xsl:template match="article/conversionwarning">
</xsl:template>

<xsl:template match="article/body">
<body  class="mediawiki ns-0 ltr">

    <div id="content_bottom">

            <h1><xsl:value-of select="ancestor::article/name"/></h1>

            <div id="related">--insertRelated--</div>

<xsl:apply-templates/>

        <br /><br />

    </div>

</body>
</xsl:template>

<xsl:template match="body/template">
</xsl:template>

<xsl:template match="body/figure">
</xsl:template>

<xsl:template match="body/p">
  <p>
    <xsl:apply-templates/>
  </p>
</xsl:template>

<xsl:template match="body/section">
    <xsl:apply-templates/><xsl:text> </xsl:text> 
</xsl:template>

<xsl:template match="body/section/title">
<h2>
    <xsl:apply-templates/>
</h2>
</xsl:template>

<xsl:template match="body/section/section/title">
<h3>
    <xsl:apply-templates/>
</h3>
</xsl:template>

<xsl:template match="body/section/section/section/title">
<h4>
    <xsl:apply-templates/>
</h4>
</xsl:template>

<xsl:template match="p">
  <p>
    <xsl:apply-templates/>
  </p>
</xsl:template>

<xsl:template match="normallist">
  <ul>
    <xsl:apply-templates/>
  </ul>
</xsl:template>

<xsl:template match="normallist/item">
  <li>
    <xsl:apply-templates/>
  </li>
</xsl:template>

<xsl:template match="table">
  <table class="{@class}"> 
  <xsl:apply-templates/>
  </table>
  <br/> 
</xsl:template>

<xsl:template match="row">
  <tr bgcolor="{@bgcolor}"><xsl:apply-templates/></tr> 
</xsl:template>

<xsl:template match="cell">
  <td rowspan="{@rowspan}" colspan="{@colspan}"><xsl:apply-templates/></td> 
</xsl:template>

<xsl:template match="collectionlink">
  <a href="javascript:loadArticle('{@xlink:href}')"> 
  <xsl:apply-templates/></a><xsl:text> </xsl:text> 
</xsl:template>

<xsl:template match="outsidelink">
  <a href="javascript:outsideLink()"> 
  <xsl:apply-templates/></a><xsl:text> </xsl:text> 
</xsl:template>

<xsl:template match="unknownlink">
  <a href="wikipediaSearch.php?query={@src}" target="search"> 
  <xsl:apply-templates/></a><xsl:text> </xsl:text> 
</xsl:template>

<xsl:template match="emph2">
  <i><xsl:apply-templates/></i><xsl:text> </xsl:text>
</xsl:template>

<xsl:template match="emph3">
  <b><xsl:apply-templates/></b><xsl:text> </xsl:text>
</xsl:template>

<xsl:template match="languagelink">
</xsl:template>

<xsl:template match="image">
</xsl:template>

<xsl:template match="caption">
</xsl:template>

<xsl:template match="br"><br></br></xsl:template>

</xsl:stylesheet>
