<!-- 
 * (C) Copyright IBM Corporation 2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Style-Type" content="text/css">
<link rel="shortcut icon" href="./favicon.ico" />
<TITLE>Welcome to DayTrader</TITLE>
</HEAD>
<BODY bgcolor="#ffffff" link="#000099">
    <TABLE style="font-size: smaller">
        <TBODY>
            <TR>
                <TD bgcolor="#c93333" align="left" width="640"
                    height="10"><B><FONT color="#ffffff">DayTrader
                            Configuration</FONT></B></TD>
                <TD align="center" bgcolor="#000000" width="100"
                    height="10"><FONT color="#ffffff"><B>DayTrader</B></FONT></TD>
            </TR>
            <TR>
                <TD colspan="6">
                    <HR>
                </TD>
            </TR>
            <TR>
                <TD colspan="6"></TD>
            </TR>
        </TBODY>
    </TABLE>

    <%
        String status;
        status = (String) request.getAttribute("status");
        if (status != null) {
    %>
    <TABLE width="740" height="30">
        <TBODY>
            <TR>
                <TD></TD>
                <TD><FONT color="#ff0033"> <%
     out.print(status);
 %>
                </FONT></TD>
                <TD></TD>
            </TR>
        </TBODY>
    </TABLE>
    <%
        }
    %>

    <FORM action="config" method="POST">
        <INPUT type="hidden" name="action" value="updateConfig">

        <TABLE border="1" width="740">
            <TBODY>
                <TR>
                    <TD colspan="2">The current DayTrader runtime
                        configuration is detailed below. View and
                        run-time parameters. &nbsp;<BR>
                        <BR> <B>NOTE: </B>Parameters can only be changed by setting environment variables.<BR>
                        <HR>
                    </TD>
                </TR>
                <TR>
                    <TD colspan="2" align="center"><B>Miscellaneous
                            Settings</B></TD>
                </TR>
                <TR>
                    <TD align="left"><B>Max Users </B><BR>
                        <INPUT readonly size="25" type="text" name="MaxUsers"
                        value="${config.maxUsers}"><BR>
                        <B>Max Quotes</B><BR> <INPUT readonly
                        size="25" type="text" name="MaxQuotes"
                        value="${config.maxQuotes}"></TD>
                    <TD>By default the DayTrader database is
                        populated with 15,000 users (uid:0 - uid:199)
                        and 10,000 quotes (s:0 - s:399). <BR>
                    </TD>
                </TR>
                <TR>
                  <TD align="left"><B>Max Holdings </B><BR>
                      <INPUT readonly size="25" type="text" name="MaxHoldings"
                      value="${config.maxHoldings}"></TD>
                  <TD>By default the DayTrader database is
                      populated with 15,000 users (uid:0 - uid:199)
                      and 10,000 quotes (s:0 - s:399). <BR>
                  </TD>
              </TR>
                 <TR>
                    <TD align="left"><INPUT onclick="return false;" type="checkbox"
                      checked=${config.displayOrderAlerts}
                        name="DisplayOrderAlerts"> <B><FONT
                            size="-1">Display Order Alerts</FONT></B><BR></TD>
                    <TD>Display completed order alerts.<BR>
                    </TD>
                </TR>                
            </TBODY>
        </TABLE>

        <TABLE width="740" height="54" style="font-size: smaller">
            <TBODY>
                <TR>
                    <TD colspan="2">
                        <HR>
                    </TD>
                </TR>
                <TR>
                    <TD colspan="2"></TD>
                </TR>
                <TR>
                    <TD bgcolor="#c93333" align="left" width="640"
                        height="10"><B><FONT color="#ffffff">DayTrader
                                Configuration</FONT></B></TD>
                    <TD align="center" bgcolor="#000000" width="100"
                        height="10"><FONT color="#ffffff"><B>DayTrader</B></FONT></TD>
                </TR>
            </TBODY>
        </TABLE>
    </FORM>
</BODY>
</HTML>
