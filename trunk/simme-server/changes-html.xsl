<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
     xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- This stylesheet produces HTML in utf-8 -->
  <xsl:output method="html"
              doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
              encoding="utf-8"/>

  <xsl:template match="/">
    <html>
      <xsl:variable name="title" select="changelog/@title"/>
      <head>
        <title><xsl:value-of select="$title"/></title>
        <style type="text/css">
          table       { border-collapse: collapse; }
          tr          { border: 1px solid; }
          tr.noborder { border: none; }
          td          { vertical-align: top;
                        border-left: dotted 1px;
                        padding:0.5em;}
          th          { text-align: left; }
          .files      { max-width: 100px; font-size: smaller;}
          ul          { padding-left: 1em;}
          dl,dt,ul    { margin-top: 0px; padding-top: 0px;}
          dd          { margin-left: 1em;}
        </style>
      </head>

      <body>
        <h1><xsl:value-of select="$title"/></h1>
        <xsl:for-each select="changelog/release">
          <xsl:sort select="@date" order="descending"/>
          <h2>Release <xsl:value-of select="@name"/></h2>
          <xsl:value-of select="@date"/>

          <xsl:apply-templates select="changes"/>
        </xsl:for-each>
      </body>
    </html>
  </xsl:template>


  <xsl:template match="changes">
    <h4>Changes in this release</h4>
    <table>
      <thead>
        <tr>
          <th>Description</th><th>Bugs</th>
        </tr>
      </thead>
      <xsl:for-each select="change">
        <tr>
          <td><xsl:apply-templates select="description"/></td>
          <td><xsl:apply-templates select="bugs"/></td>
        </tr>
      </xsl:for-each>
    </table>
  </xsl:template>

  <xsl:template match="bugs">
    <dl>
      <xsl:for-each select="bug">
        <xsl:sort select="@id"/>
        <dt><xsl:text>Bug </xsl:text>
          <a>
            <xsl:attribute name="href">
              <xsl:text>https://developer.berlios.de/bugs/?func=detailbug&amp;group_id=730&amp;bug_id=</xsl:text>
              <xsl:choose>
                <xsl:when test="substring-before(@id, ',') = ''">
                  <xsl:value-of select="@id"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="substring-before(@id, ',')"/>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
            <xsl:value-of select="@id"/>
          </a>
          <xsl:text>: </xsl:text>
        </dt>
        <dd>
          <xsl:apply-templates/>
        </dd>
      </xsl:for-each>
    </dl>
  </xsl:template>

  <xsl:template match="@*|node()">
     <xsl:copy>
        <xsl:apply-templates select="@*|node()"/>
     </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
