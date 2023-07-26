package com.sreeeservices.ui;

import com.sreeeservices.model.Company;

public class HeaderContent {

    public static String getHeaderContent(final Company company) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "\t<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css\">\n" +
                "    <title>Responsive Table</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        .topnav {\n" +
                "            background-color: #333;\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "\n" +
                "        .topnav a {\n" +
                "            float: left;\n" +
                "            color: #f2f2f2;\n" +
                "            text-align: center;\n" +
                "            padding: 14px 16px;\n" +
                "            text-decoration: none;\n" +
                "            font-size: 17px;\n" +
                "        }\n" +
                "\n" +
                "        .topnav a:hover {\n" +
                "            background-color: #ddd;\n" +
                "            color: black;\n" +
                "        }\n" +
                "\n" +
                "        .topnav a.active {\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "        }\n" +
                "\n" +
                "        /* Table Styles */\n" +
                "        .table-container {\n" +
                "            max-width: 100%;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 20px;\n" +
                "            background-color: #f9f9f9;\n" +
                "            border-radius: 5px;\n" +
                "            box-shadow: 0 0 5px rgba(0, 0, 0, 0.3);\n" +
                "            overflow-x: auto;\n" +
                "        }\n" +
                "\n" +
                "        table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "        }\n" +
                "\n" +
                "        th, td {\n" +
                "            padding: 8px;\n" +
                "            text-align: left;\n" +
                "            border-bottom: 1px solid #ddd;\n" +
                "        }\n" +
                "\n" +
                "        th {\n" +
                "            background-color: #f2f2f2;\n" +
                "        }\n" +
                "\n" +
                "\t\t.icon {\n" +
                "            margin-right: 8px;\n" +
                "        }\n" +
                "        /* Responsive Styles */\n" +
                "        @media screen and (max-width: 100%) {\n" +
                "            .topnav a {\n" +
                "                display: block;\n" +
                "                text-align: center;\n" +
                "            }\n" +
                "\n" +
                "            .table-container {\n" +
                "                padding: 10px;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                getActiveClass(company);

    }

    private static String getActiveClass(final Company company) {
        if (company.equals(Company.BUTTERFLY)) {
            return "    <div class=\"topnav\">\n" +
                    "        <a href=\"#\"><i class=\"fas fa-home icon\"></i>Home</a>\n" +
                    "        <a href=\"/viewComplaints/glen\"><i class=\"fas fa-info-circle icon\"></i>Glen</a>\n" +
                    "        <a class=\"active\" href=\"/viewComplaints/butterfly\"><i class=\"fas fa-envelope icon\"></i>Butterfly</a>\n" +
                    "        <a href=\"/viewComplaints/symphony\"><i class=\"fas fa-sign-in-alt icon\"></i>Symphony</a>\n" +
                    "    </div>\n" ;
        }
        if (company.equals(Company.SYMPHONY)) {
            return "    <div class=\"topnav\">\n" +
                    "        <a href=\"#\"><i class=\"fas fa-home icon\"></i>Home</a>\n" +
                    "        <a href=\"/viewComplaints/glen\"><i class=\"fas fa-info-circle icon\"></i>Glen</a>\n" +
                    "        <a  href=\"/viewComplaints/butterfly\"><i class=\"fas fa-envelope icon\"></i>Butterfly</a>\n" +
                    "        <a class=\"active\" href=\"/viewComplaints/symphony\"><i class=\"fas fa-sign-in-alt icon\"></i>Symphony</a>\n" +
                    "    </div>\n" ;
        }
        if (company.equals(Company.GLEN)) {
            return "    <div class=\"topnav\">\n" +
                    "        <a href=\"#\"><i class=\"fas fa-home icon\"></i>Home</a>\n" +
                    "        <a class=\"active\"  href=\"/viewComplaints/glen\"><i class=\"fas fa-info-circle icon\"></i>Glen</a>\n" +
                    "        <a  href=\"/viewComplaints/butterfly\"><i class=\"fas fa-envelope icon\"></i>Butterfly</a>\n" +
                    "        <a href=\"/viewComplaints/symphony\"><i class=\"fas fa-sign-in-alt icon\"></i>Symphony</a>\n" +
                    "    </div>\n" ;
        }
        return "    <div class=\"topnav\">\n" +
                "        <a class=\"active\" href=\"#\"><i class=\"fas fa-home icon\"></i>Home</a>\n" +
                "        <a   href=\"/viewComplaints/glen\"><i class=\"fas fa-info-circle icon\"></i>Glen</a>\n" +
                "        <a  href=\"/viewComplaints/butterfly\"><i class=\"fas fa-envelope icon\"></i>Butterfly</a>\n" +
                "        <a href=\"/viewComplaints/symphony\"><i class=\"fas fa-sign-in-alt icon\"></i>Symphony</a>\n" +
                "    </div>\n" ;
    }
}
