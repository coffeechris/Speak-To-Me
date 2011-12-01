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
</head>
   <xsl:apply-templates/>
 </html>
</xsl:template>

<xsl:template match="article/name">
</xsl:template>

<xsl:template match="article/conversionwarning">
</xsl:template>

<xsl:template match="article/body">
<body>
    <div>
		<xsl:apply-templates/>
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
  <a href="view_document.htm?id={@xlink:href}"> 
  <xsl:apply-templates/></a><xsl:text> </xsl:text> 
</xsl:template>

<xsl:template match="outsidelink">
  <a href="{@xlink:href}"> 
  <xsl:apply-templates/></a><xsl:text> </xsl:text> 
</xsl:template>

<xsl:template match="unknownlink">
  <xsl:apply-templates/><xsl:text> </xsl:text> 
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
