<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:str="http://xsltsl.org/string">

  <!-- This stylesheet produces HTML in iso-8859-1 -->
  <xsl:output method="html"
      doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
      encoding="iso-8859-1"/>

  <xsl:variable name="page-id" select="page/@id"/>

  <xsl:template match="/">
    <html>

      <xsl:variable name="title" select="page/meta/title"/>
      <head>
        <title>
          <xsl:value-of select="$title"/>
        </title>
        <link rel="stylesheet" href="style/styles.css" media="screen"/>
        <link rel="stylesheet" href="style/print.css" media="print"/>
      </head>

      <body>
        <table width="100%">
          <tbody>
            <tr>
              <td id="menu">
                <table>
                  <div id="nav">Quick Nav</div>
                  <tr>
                    <xsl:call-template name="menu">
                      <xsl:with-param name="page">index.html</xsl:with-param>
                      <xsl:with-param name="name">Main</xsl:with-param>
                    </xsl:call-template>
                  </tr>
                  <tr>
                    <xsl:call-template name="menu">
                      <xsl:with-param name="page">devel.html</xsl:with-param>
                      <xsl:with-param name="name">DevDocs</xsl:with-param>
                    </xsl:call-template>
                  </tr>
                  <tr>
                    <xsl:call-template name="menu">
                      <xsl:with-param name="page">files.html</xsl:with-param>
                      <xsl:with-param name="name">Files</xsl:with-param>
                    </xsl:call-template>
                  </tr>
                </table>
              </td>
              <td width="100%">

            <!-- content of the page -->

                <xsl:apply-templates select="page/body"/>

            <!-- end of content -->
              </td>
            </tr>
          </tbody>

        </table>


        <div id="info">
        Any comments? Send <a
              href="mailto:kariem@users.berlios.de?subject=[simme]%20web%20-%20">
        us</a> an email!
      </div>
      </body>
    </html>
  </xsl:template>


  <xsl:template match="section">
    <xsl:param name="level">
      <xsl:call-template name="str:count-substring">
        <xsl:with-param name="text">
          <xsl:number count="section" level="multiple"/>
        </xsl:with-param>
        <xsl:with-param name="chars" select="'.'"/>
      </xsl:call-template>
    </xsl:param>
    <xsl:element name="h{$level+1}">
      <xsl:apply-templates select="title/node()"/>
    </xsl:element>

    <xsl:apply-templates/>
  </xsl:template>


  <!--
       Tag duplication in order to retain HTML formatting
  -->

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

	<!-- external links open in _blank
  <xsl:template match="a">
    <xsl:copy>
      <xsl:if test="starts-with(@href, 'http://')">
        <xsl:if test="not(@target)">
          <xsl:attribute name="target">_blank</xsl:attribute>
        </xsl:if>
      </xsl:if>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
	 -->


  <xsl:template name="menu">
    <xsl:param name="page"/>
    <xsl:param name="name"/>
    <xsl:param name="class"/>

    <td>
      <!-- see if there is a class attribute -->
      <div>
        <xsl:if test="$class != ''">
          <xsl:attribute name="class">
            <xsl:value-of select="$class"/>
          </xsl:attribute>
        </xsl:if>
      <!-- check if link is necessary -->
        <xsl:choose>
          <xsl:when test="$page-id = substring-before($page, '.')">
            <div class="selected">
              <xsl:value-of select="$name"/>
            </div>
          </xsl:when>
          <xsl:otherwise>
            <a href="{$page}">
              <xsl:value-of select="$name"/>
            </a>
          </xsl:otherwise>
        </xsl:choose>
      </div>
    </td>
  </xsl:template>


  <!-- copied from xsltsl.org -->

  <xsl:template name="str:count-substring">
    <xsl:param name="text"/>
    <xsl:param name="chars"/>

    <xsl:choose>
      <xsl:when test="string-length($text) = 0 or string-length($chars) = 0">
        <xsl:text>0</xsl:text>
      </xsl:when>
      <xsl:when test="contains($text, $chars)">
        <xsl:variable name="remaining">
          <xsl:call-template name="str:count-substring">
            <xsl:with-param name="text" select="substring-after($text, $chars)"/>
            <xsl:with-param name="chars" select="$chars"/>
          </xsl:call-template>
        </xsl:variable>
        <xsl:value-of select="$remaining + 1"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>0</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>
