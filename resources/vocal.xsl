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
    <script type="text/javascript" src="js/prototype-1.6.0.3.js">
function tempCall() {
}    
    </script>
    <script type="text/javascript">
function closeDoc() {
	//log user action with ajax call
	new Ajax.Request('close_document.htm', {
		method: 'get',
		parameters: {id: <xsl:value-of select="name/@id"/>}
	});
}

//scroll detections
var isScrolling = false;
var scrollOn = false;
function startScrolling() {
	if (!isScrolling) {
		//log user action with ajax call
		new Ajax.Request('start_scroll_document.htm', {
			method: 'get',
			parameters: {id: <xsl:value-of select="name/@id"/>}
		});
	}
	scrollOn = true;
	isScrolling = true;
}

function stopScrolling() {
	if(scrollOn) {
		scrollOn = false;
	}
	else if (isScrolling) {
		isScrolling = false;
		//log user action with ajax call
		new Ajax.Request('stop_scroll_document.htm', {
			method: 'get',
			parameters: {id: <xsl:value-of select="name/@id"/>}
		});
	}
	
	setTimeout("stopScrolling()",500);
}

function selectedText() {
    selText = window.getSelection();
    //log user action with ajax call
	new Ajax.Request('record_selected_text.htm', {
		method: 'post',
		parameters: {id: <xsl:value-of select="name/@id"/>,
		             text: selText.toString()}
	});
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
<body onunload="closeDoc();" onload="stopScrolling();" onscroll="startScrolling();" onclick="selectedText();">
	<div>
		<button onclick="window.close()">Close</button>
    </div>
    <div id="content_bottom">
        <h1><xsl:value-of select="ancestor::article/name"/></h1>
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
