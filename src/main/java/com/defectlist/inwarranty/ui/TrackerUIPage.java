package com.defectlist.inwarranty.ui;

import com.defectlist.inwarranty.model.FinanceType;
import com.defectlist.inwarranty.model.FinancialDetails;
import com.defectlist.inwarranty.model.FinancialMonth;
import com.defectlist.inwarranty.utils.MonthUtils;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TrackerUIPage {

    public static String getFirstPage() {
        return "<html>\n" +
                "\t<head>\n" +
                "\t\t<title>:: Generate Defectives List ::</title>\n" +
                "\t\t<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">\n" +
                "<style>" +
                ".switch {\n" +
                "  position: relative;\n" +
                "  display: inline-block;\n" +
                "  width: 52px;\n" +
                "  height: 24px;\n" +
                "}\n" +
                "\n" +
                ".switch input { \n" +
                "  opacity: 0;\n" +
                "  width: 0;\n" +
                "  height: 0;\n" +
                "}\n" +
                "\n" +
                ".slider {\n" +
                "  position: absolute;\n" +
                "  cursor: pointer;\n" +
                "  top: 0;\n" +
                "  left: 0;\n" +
                "  right: 0;\n" +
                "  bottom: 0;\n" +
                "  background-color: #ccc;\n" +
                "  -webkit-transition: .4s;\n" +
                "  transition: .4s;\n" +
                "}\n" +
                "\n" +
                ".slider:before {\n" +
                "  position: absolute;\n" +
                "  content: \"\";\n" +
                "  height: 18px;\n" +
                "  width: 18px;\n" +
                "  left: 4px;\n" +
                "  bottom: 3px;\n" +
                "  background-color: white;\n" +
                "  -webkit-transition: .4s;\n" +
                "  transition: .4s;\n" +
                "}\n" +
                "\n" +
                "input:checked + .slider {\n" +
                "  background-color: #2196F3;\n" +
                "}\n" +
                "\n" +
                "input:focus + .slider {\n" +
                "  box-shadow: 0 0 1px #2196F3;\n" +
                "}\n" +
                "\n" +
                "input:checked + .slider:before {\n" +
                "  -webkit-transform: translateX(26px);\n" +
                "  -ms-transform: translateX(26px);\n" +
                "  transform: translateX(26px);\n" +
                "}\n" +
                "\n" +
                "/* Rounded sliders */\n" +
                ".slider.round {\n" +
                "  border-radius: 35px;\n" +
                "}\n" +
                "\n" +
                ".slider.round:before {\n" +
                "  border-radius: 50%;\n" +
                "}</style>" +
                "<script type=text/javascript>" +
                "function validate() {" +
                "if (document.getElementById('username').value==''){" +
                "alert('Please enter username');" +
                "document.getElementById('username').focus();" +
                "} else if (document.getElementById('pin').value==''){" +
                "alert('Please enter pin');" +
                "document.getElementById('pin').focus();" +
                "} else {" +
                "document.getElementById('billGenerator').submit();" +
                "}}" +
                "</script>" +
                "\t</head>\n" +
                "\t<body>\n" +
                "<center><b><h4>Login page</h4></b></center><hr>" +
                "\t\t\t\t<div class=\"form-group\">" +
                "<form name='billGenerator' id='billGenerator' method='POST' action='/app/v1/tracker/login'>" +
                "<center>" +
                "<div class=\"container\" style=\"width:500px; align:center;\">\n" +
                "\t\t\t\t<div class=\"form-group\">\n" +
                "\t\t\t\t  <label class=\"control-label col-sm-4\" style=\"margin-top:0.5%;\" for=\"Username\">Username</label>\n" +
                "\t\t\t\t  <div class=\"col-sm-6\">  " +
                " <input class=form-control placeholder=username type=text id=username name=username></div>" +
                "<div class=\"col-sm-2\" style=\"margin-left:0px;\" id=error-username></div>" +
                "\t\t\t\t<div class=\"form-group\">\n" +
                "\t\t\t\t  <label class=\"control-label col-sm-4\" style=\"margin-top:0.5%;\" for=\"Pin\">Pin</label>\n" +
                "\t\t\t\t  <div class=\"col-sm-6\">  " +
                "<input class=form-control placeholder=pin type=number id=pin name=pin></div>" +
                "<div class=\"col-sm-2\" style=\"margin-left:0px;\" id=error-password></div>" +
                "\t\t\t\t<hr><div class=\"form-group\">\n" +
                "\t\t\t\t  <div class=\"col-sm-12\">  " +
                "<input class='btn btn-default' style='align:right;' name='Login' " +
                "value='Login' type=button onclick=javascript:validate()></div></div>" +
                "</table></center>" +
                "</form>\n" +
                "</body>\n" +
                "</html>";
    }

    public static String getDetailsPage(final FinancialMonth financialMonth,
                                        final URL editImageUrl) {
        final String count = String.valueOf(financialMonth.getDetails().size());
        return "<html>\n" +
                "    <head>\n" +
                "        <title>My Financial Tracker :: View Details</title>\n" +
                "        <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">\n" +
                "        <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js\"></script>\n" +
                "        <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js\"></script>\n" +
                "        <script type=\"text/javascript\">\n" +
                "            var count = " + count + ";\n" +
                "            var valueMap = new Map();\n" +
                "            var editMap = new Map();\n" +
                "\n" +
                "            function deleteRow(id) {\n" +
                "                document.getElementById('container' + id).innerHTML = '';\n" +
                "                valueMap.delete('amount' + id);\n" +
                "                valueMap.delete('type' + id);\n" +
                "                valueMap.delete('description' + id);\n" +
                "            }\n" +
                "            function deleteEditRow(id) {\n" +
                "                document.getElementById('container' + id).innerHTML = '';\n" +
                "                valueMap.delete('amount' + id);\n" +
                "                valueMap.delete('type' + id);\n" +
                "                valueMap.delete('description' + id);\n" +
                "                editMap.delete('edit' + id);\n" +
                "            }\n" +
                "\n" +
                "function loadData(month) {\n" +
                "                document.getElementById('my-form-month').submit();\n" +
                "            }" +
                "            function update(id, value) {\n" +
                "                valueMap.set(id, value);\n" +
                "            }\n" +
                "\n" +
                "            function updateRecord(id, description, amount, type) {\n" +
                "                   if (!editMap.has('edit' + id)) {\n" +
                "                   editMap.set('edit' + id, 'exists');" +
                        "document.getElementById('update-record').innerHTML = document.getElementById('update-record').innerHTML + \"<div id='container\" + id + \"' class='container'>\" +\n" +
                        "                    \"<div class='row'>\" + \n" +
                        "                      \"<div class='col-sm-2'>\" + \n" +
                        "                            \"Edit a Record - \" + id + \n" +
                        "                        \"</div>\" + \n" +
                        "                        \"<div class='col-sm-2'>\" + \n" +
                        "                              \"<input type='hidden'  name='id\" + id + \"' id='id\" + id + \"' value='\" + id + \"'> \" + \n" +
                        "                            \"<input placeholder='description' value='\" + description + \"' onchange='javascript:update(this.id, this.value);' type='text' name='description\" + id + \"' id='description\" + id + \"'>\" +" +
                        "                        \"</div>\" + \n" +
                        "                        \"<div class='col-sm-2'>\" + \n" +
                        "                            \"<input value='\" + amount + \"' onload='javascript:update(this.id, this.value);' onchange='javascript:update(this.id, this.value);' placeholder='Amount for Record - \" + id + \"' class='btn' id='amount\" + id + \"' name='amount\" + id + \"' type='number'>\" + \n" +
                        "                        \"</div>\" + \n" +
                        "                        \"<div class='col-sm-2'>\" +\n" +
                        "                            \"<select class='btn' value=\" + type + \" onchange='javascript:update(this.id, this.value);' name='type\" + id + \"' id='type\" + id + \"'>\" +\n" +
                        "                                \"<option value='0'>Select Type for Record - \" + id + \"</option>\" +\n" +
                        "                                \"<option value='CREDIT'>Credit for Record - \" + id + \" </option>\" +\n" +
                        "                                \"<option value='DEBIT'>Debit for Record - \" + id + \" </option>\" +\n" +
                        "                                \"<option value='INVESTMENT'>Investment with Debit for Record - \" + id + \" </option>\" +\n" +
                        "                                \"<option value='INVESTMENT_DEFAULT'>Investment without Debit for Record - \" + id + \" </option>\" +\n" +
                        "                            \"</select>\" +\n" +
                        "                        \"</div>\" +\n" +
                        "                        \"<div class='col-sm-2'>\" + \n" +
                        "                            \"<button onclick='javascript:deleteEditRow(\" + id + \");' class='btn btn-danger' id='amount\" + id + \"' name='delete\" + id + \"' type='button'>Delete</button>\" + \n" +
                        "                        \"</div>\" + \n" +
                        "                    \"</div><hr>\" + \n" +
                        "                \"</div>\";\n" +
                        "                loadOldValues();\n" +
                "                       }" +
                        "            }" +
                "\n" +
                "function reload() {" +
                "location.reload();" +
                "}" +
                "" +
                "            function addField() {\n" +
                "                count++;\n" +
                "                document.getElementById('field_holder').innerHTML = document.getElementById('field_holder').innerHTML + \n" +
                "                \"<div id='container\" + count + \"' class='container'>\" +\n" +
                "                    \"<div class='row'>\" + \n" +
                "                      \"<div class='col-sm-2'>\" + \n" +
                "                            \"Add a Record - \" + count + \n" +
                "                        \"</div>\" + \n" +
                "                        \"<div class='col-sm-2'>\" + \n" +
                "                            \"<input placeholder='description' onchange='javascript:update(this.id, this.value);' type='text' name='description\" + count + \"' id='description\" + count + \"'>\" +" +
                "                        \"</div>\" + \n" +
                "                        \"<div class='col-sm-2'>\" + \n" +
                "                            \"<input onchange='javascript:update(this.id, this.value);' placeholder='Amount for Record - \" + count + \"' class='btn' id='amount\" + count + \"' name='amount\" + count + \"' type='number'>\" + \n" +
                "                        \"</div>\" + \n" +
                "                        \"<div class='col-sm-2'>\" +\n" +
                "                            \"<select class='btn' onchange='javascript:update(this.id, this.value);' name='type\" + count + \"' id='type\" + count + \"'>\" +\n" +
                "                                \"<option value='0' selected>Select Type</option>\" +\n" +
                "                                \"<option value='CREDIT'>Credit </option>\" +\n" +
                "                                \"<option value='DEBIT'>Debit</option>\" +\n" +
                "                                \"<option value='INVESTMENT'>Investment with Debit </option>\" +\n" +
                "                                \"<option value='INVESTMENT_DEFAULT'>Investment without Debit</option>\" +\n" +
                "                            \"</select>\" +\n" +
                "                        \"</div>\" +\n" +
                "                        \"<div class='col-sm-2'>\" + \n" +
                "                            \"<button onclick='javascript:deleteRow(\" + count + \");' class='btn btn-danger' id='amount\" + count + \"' name='delete\" + count + \"' type='button'>Delete</button>\" + \n" +
                "                        \"</div>\" + \n" +
                "                    \"</div><hr>\" + \n" +
                "                \"</div>\";\n" +
                "                loadOldValues();\n" +
                "            }\n" +
                "            function loadOldValues() {\n" +
                "                for (const [key, value] of valueMap.entries()) {\n" +
                "                    document.getElementById(key).value = value;\n" +
                "                }\n" +
                "            }\n" +
                "        </script>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <form name='my-form-month' id='my-form-month' action=/app/v1/tracker/load method=post>\n" +
                "           <center>" +
                "                        <div class='container col-sm-12'>" +
                "                            <select class='btn btn-info' onchange='javascript:loadData(this.id, this.value);' name='month' id='month'>" +
                populateMonths(financialMonth.getMonth()) +
                "                            </select>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<button type=\"button\" class=\"btn btn-info\" onclick=\"javascript:reload()\">\n" +
                "                Reload\n" +
                "            </button>" +
                "                        </div>" +
                "</center><br><hr>" +
                "</form>" +
                "<form name=my-form action=/app/v1/tracker/create method=\"post\">\n" +
                "           <!-- <center>-->\n" +
                "<input type=hidden name='month' value='" + financialMonth.getMonth() + "'>" +
                populateMonthRows(financialMonth, editImageUrl) +
                "            <div id=\"field_holder\">\n" +
                "\n" +
                "            </div>\n" +
                "            <div id=\"update-record\">\n" +
                "\n" +
                "            </div>\n" +
                "            <hr>\n" +
                "           <center> <button type=\"button\" class=\"btn btn-success\" onclick=\"javascript:addField()\">\n" +
                "                Add Record\n" +
                "            </button>\n" +
                "            <button type=\"submit\" class=\"btn btn-primary\" onclick=\"javascript:submit()\">\n" +
                "                Submit\n" +
                "            </button>\n" +
                "            </center>\n" +
                "        \n" +
                "        </form>\n" +
                "    </body>\n" +
                "</html>";
    }

    private static String populateMonthRows(final FinancialMonth financialMonth,
                                            final URL editImageUrl) {
        final long credit = financialMonth.getDetails().stream()
                .filter(details -> "CREDIT".equalsIgnoreCase(details.getFinanceType()))
                .mapToLong(FinancialDetails::getAmount)
                .sum();
        final long debit = financialMonth.getDetails().stream()
                .filter(details -> "DEBIT".equalsIgnoreCase(details.getFinanceType()))
                .mapToLong(FinancialDetails::getAmount)
                .sum();
        final long investment = financialMonth.getDetails().stream()
                .filter(details -> "INVESTMENT".equalsIgnoreCase(details.getFinanceType()))
                .mapToLong(FinancialDetails::getAmount)
                .sum();
        final String rows = financialMonth.getDetails().stream()
                .sorted(Comparator.comparing(FinancialDetails::getFinanceType, Comparator.reverseOrder())
                        .thenComparing(FinancialDetails::getAmount, Comparator.reverseOrder()))
                .map(details -> {
                    final String buttonType = details.getFinanceType().equals("CREDIT")
                            ? "#42f59b"
                            : details.getFinanceType().equals("DEBIT") ?  "#f56358" : "#3d9be3";
                    return "<div style='font-size:15px; class='border border-bottom-0 container'>" +
                            "   <div class='row container'>" +
                            "       <div class='col-sm-2'></div>" +
                            "       <div style='outline: 1px dotted;background-color:" + buttonType + ";' class='col-sm-6'>" +
                            "           " + details.getDescription() +
                            "       </div>" +
                            "       <div style='outline: 1px dotted;background-color:" + buttonType + ";' class='col-sm-3'>" +
                            "           " + details.getAmount() +
                            "       </div>" +
                            "       <div style='outline: 1px dotted;' class='col-sm-1'>" +
                            "           <a href='javascript:void()' onclick='javascript:updateRecord(\"" + details.getId() + "\", \"" + details.getDescription() + "\", \"" +
                            details.getAmount() + "\", \""+ details.getFinanceType() + "\");'><img width=20px height=20px src='" + editImageUrl + "'> Edit</a>" +
                            "       </div>" +
                            "   </div>" +
                            "</div>";
                })
                .collect(Collectors.joining()) + "<hr>";
        final long finalAmount = credit - debit - investment;
        final String color = finalAmount < 0 ? "#f56358" : "#42f59b";
        return rows.concat("<hr><div style='font-size:15px;background-color:" + color + "' class='border border-bottom-0 container'>" +
                "   <div class='row container'>" +
                "       <div class='col-sm-4'>" +
                "           Final Amount" +
                "       </div>" +
                "   <div class='row container'>" +
                "       <div class='col-sm-4'>" +
                "           " + finalAmount +
                "       </div>" +
                "</div>" +
                "</div>" +
                "</div><hr>");
    }

    private static String populateMonths(final String month) {
        final List<String> months = MonthUtils.monthsList();
        return months.stream()
                .map(m -> {
                    final String selected = m.equalsIgnoreCase(month) ? "selected" : "";
                    return "<option value='" + m + "' " + selected + " >" + m + "</option>";
                })
                .collect(Collectors.joining());
    }
}
