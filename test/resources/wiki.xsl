<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink" exclude-result-prefixes="xlink">

<xsl:output method="html"/>

<xsl:template match="article">
   <html><body>
      <xsl:apply-templates/>
   </body></html>
</xsl:template>

<xsl:template match="name">
   <h1><xsl:apply-templates/></h1><br></br>
</xsl:template>

<xsl:template match="conversionwarning"></xsl:template>

<xsl:template match="/"><xsl:apply-templates/></xsl:template>

<xsl:template match="body">
   <p><xsl:apply-templates/></p>
</xsl:template>

<xsl:template match="collectionlink">
   <a href="{@xlink:href}"><xsl:value-of select="."/></a>
</xsl:template>

<xsl:template match="p">
   <p><blockquote><xsl:apply-templates/></blockquote></p>
</xsl:template>

<xsl:template match="section">
   <p><xsl:apply-templates/></p>
</xsl:template>

<xsl:template match="title">
   <p><strong><xsl:apply-templates/></strong></p>
</xsl:template>


<xsl:template match="emph2">
   <i><xsl:apply-templates/></i>
</xsl:template>

<xsl:template match="normallist">
   <ul type="square"><xsl:apply-templates/></ul>
</xsl:template>

<xsl:template match="item">
   <li><xsl:apply-templates/></li>
</xsl:template>

<xsl:template match="br">
   <br><xsl:apply-templates/></br>
</xsl:template>

<xsl:template match="emph3">
   <em><xsl:apply-templates/></em>
</xsl:template>

<xsl:template match="emph4">
   <em><xsl:apply-templates/></em>
</xsl:template>

<xsl:template match="emph1">
   <em><xsl:apply-templates/></em>
</xsl:template>

<xsl:template match="unknownlink">
   <xsl:apply-templates/>
</xsl:template>

</xsl:stylesheet>
