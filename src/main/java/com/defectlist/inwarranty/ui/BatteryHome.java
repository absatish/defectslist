package com.defectlist.inwarranty.ui;

import com.defectlist.inwarranty.resource.BatteryDischargeOptimizationResource;

public class BatteryHome {

    public static String getHomePage() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Dynamic Cycle Fields</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f0f0f0;\n" +
                "            margin: 0;\n" +
                "            padding: 20px;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            min-height: 100vh;\n" +
                "            flex-direction: column; /* Ensure content is stacked vertically */\n" +
                "        }\n" +
                "        .form-container {\n" +
                "            background-color: white;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                "            width: 80%;\n" +
                "            max-width: 600px;\n" +
                "            overflow: hidden;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .form-container h2 {\n" +
                "            margin-top: 0;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .form-group {\n" +
                "            margin-bottom: 15px;\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        .form-group label {\n" +
                "            display: block;\n" +
                "            margin-bottom: 5px;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .form-group input[type=\"number\"], .form-group input[type=\"text\"] {\n" +
                "            width: calc(100% - 20px);\n" +
                "            padding: 10px;\n" +
                "            box-sizing: border-box;\n" +
                "            border: 1px solid #ccc;\n" +
                "            border-radius: 4px;\n" +
                "            font-size: 14px;\n" +
                "            transition: border-color 0.3s, box-shadow 0.3s;\n" +
                "        }\n" +
                "        .form-group input[type=\"number\"]:focus, .form-group input[type=\"text\"]:focus {\n" +
                "            outline: none;\n" +
                "            border-color: #28a745;\n" +
                "            box-shadow: 0 0 5px #28a745;\n" +
                "        }\n" +
                "        .form-group input[type=\"text\"][readonly] {\n" +
                "            background-color: #f0f0f0;\n" +
                "        }\n" +
                "        .form-group input[type=\"submit\"] {\n" +
                "            background-color: #007bff;\n" +
                "            color: white;\n" +
                "            padding: 10px 15px;\n" +
                "            border: none;\n" +
                "            border-radius: 4px;\n" +
                "            cursor: pointer;\n" +
                "            font-size: 14px;\n" +
                "            transition: background-color 0.3s;\n" +
                "        }\n" +
                "        .form-group input[type=\"submit\"]:hover {\n" +
                "            background-color: #0056b3;\n" +
                "        }\n" +
                "        .cycle-section {\n" +
                "            background-color: #f0f0f0;\n" +
                "            padding: 15px;\n" +
                "            margin-bottom: 20px;\n" +
                "            border-radius: 6px;\n" +
                "            display: flex;\n" +
                "            justify-content: space-between; /* Display horizontally */\n" +
                "            align-items: center;\n" +
                "        }\n" +
                "        .cycle-section:nth-child(even) {\n" +
                "            background-color: white;\n" +
                "        }\n" +
                "        .cycle-section:hover {\n" +
                "            background-color: #e0e0e0;\n" +
                "        }\n" +
                "        .cycle-section label {\n" +
                "            margin-right: 10px;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .cycle-section .styled-input {\n" +
                "            flex: 1; /* Take remaining space */\n" +
                "            padding: 8px;\n" +
                "            border: 1px solid #ccc;\n" +
                "            border-radius: 4px;\n" +
                "            font-size: 14px;\n" +
                "            transition: border-color 0.3s, box-shadow 0.3s;\n" +
                "        }\n" +
                "        .cycle-section .styled-input:focus {\n" +
                "            outline: none;\n" +
                "            border-color: #007bff;\n" +
                "            box-shadow: 0 0 5px #007bff;\n" +
                "        }\n" +
                "        .cycle-section .styled-input[readonly] {\n" +
                "            background-color: #f0f0f0;\n" +
                "        }\n" +
                "        #graphContainer {\n" +
                "            margin-top: auto; /* Pushes graph to bottom */\n" +
                "            display: none; /* Initially hide graph container */\n" +
                "        }\n" +
                "        #graph {\n" +
                "            width: 100%;\n" +
                "            height: 400px;\n" +
                "            border: 1px solid #ccc;\n" +
                "            border-radius: 4px;\n" +
                "        }\n" +
                "        .separator {\n" +
                "            margin: 20px 0;\n" +
                "            width: 100%;\n" +
                "            height: 1px;\n" +
                "            background-color: #ccc;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"form-container\">\n" +
                "        <h2>Enter Number of Cycles</h2>\n" +
                "        <form id=\"cycleForm\">\n" +
                "            <div class=\"form-group\">\n" +
                "                <label for=\"cycles\">Cycles:</label>\n" +
                "                <input type=\"number\" id=\"cycles\" name=\"cycles\" required min=\"1\">\n" +
                "            </div>\n" +
                "            <div class=\"form-group\">\n" +
                "                <input type=\"submit\" value=\"Submit\">\n" +
                "            </div>\n" +
                "        </form>\n" +
                "    </div>\n" +
                "\n" +
                "    <div id=\"cycleFieldsContainer\">\n" +
                "        <!-- Cycle fields will be added here -->\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"separator\"></div>\n" +
                "\n" +
                "    <div id=\"graphContainer\">\n" +
                "        <h2>Graph: cRate vs Final Charge</h2>\n" +
                "        <canvas id=\"graph\"></canvas>\n" +
                "        <p>Are you okay with the data? <button id=\"confirmData\">Yes</button></p>\n" +
                "    </div>\n" +
                "\n" +
                "    <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n" +
                "    <script>\n" +
                "        let cycleFormSubmitted = false; // Flag to track form submission status\n" +
                "\n" +
                "        document.getElementById('cycleForm').addEventListener('submit', function(event) {\n" +
                "            event.preventDefault(); // Prevent form submission\n" +
                "\n" +
                "            const numberOfCycles = parseInt(document.getElementById('cycles').value, 10);\n" +
                "            const cycleFieldsContainer = document.getElementById('cycleFieldsContainer');\n" +
                "            cycleFieldsContainer.innerHTML = ''; // Clear previous fields\n" +
                "\n" +
                "            for (let i = 1; i <= numberOfCycles; i++) {\n" +
                "                const cycleSection = document.createElement('div');\n" +
                "                cycleSection.className = 'cycle-section';\n" +
                "\n" +
                "                // Create cRate input\n" +
                "                const cRateLabel = document.createElement('label');\n" +
                "                cRateLabel.textContent = `cRate ${i}:`;\n" +
                "                cycleSection.appendChild(cRateLabel);\n" +
                "                const cRateInput = document.createElement('input');\n" +
                "                cRateInput.type = 'text';\n" +
                "                cRateInput.name = `cRate_${i}`;\n" +
                "                cRateInput.required = true;\n" +
                "                cRateInput.className = 'styled-input';\n" +
                "                cycleSection.appendChild(cRateInput);\n" +
                "\n" +
                "                // Create initialChargeOfTheCell input\n" +
                "                const initialChargeLabel = document.createElement('label');\n" +
                "                initialChargeLabel.textContent = `Initial Charge ${i}:`;\n" +
                "                cycleSection.appendChild(initialChargeLabel);\n" +
                "                const initialChargeInput = document.createElement('input');\n" +
                "                initialChargeInput.type = 'text';\n" +
                "                initialChargeInput.name = `initialCharge_${i}`;\n" +
                "                initialChargeInput.required = true;\n" +
                "                initialChargeInput.className = 'styled-input';\n" +
                "                if (i > 1) {\n" +
                "                    initialChargeInput.readOnly = true; // ReadOnly from the second cycle onwards\n" +
                "                }\n" +
                "                cycleSection.appendChild(initialChargeInput);\n" +
                "\n" +
                "                // Create finalChargeOfTheCell input\n" +
                "                const finalChargeLabel = document.createElement('label');\n" +
                "                finalChargeLabel.textContent = `Final Charge ${i}:`;\n" +
                "                cycleSection.appendChild(finalChargeLabel);\n" +
                "                const finalChargeInput = document.createElement('input');\n" +
                "                finalChargeInput.type = 'text';\n" +
                "                finalChargeInput.name = `finalCharge_${i}`;\n" +
                "                finalChargeInput.required = true;\n" +
                "                finalChargeInput.className = 'styled-input';\n" +
                "                cycleSection.appendChild(finalChargeInput);\n" +
                "\n" +
                "                // Update previous final charge for the next cycle\n" +
                "                finalChargeInput.addEventListener('input', function() {\n" +
                "                    if (i < numberOfCycles) {\n" +
                "                        const nextInitialChargeInput = document.querySelector(`input[name=\"initialCharge_${i + 1}\"]`);\n" +
                "                        if (nextInitialChargeInput) {\n" +
                "                            nextInitialChargeInput.value = finalChargeInput.value;\n" +
                "                        }\n" +
                "                    }\n" +
                "                    updateGraph();\n" +
                "                });\n" +
                "\n" +
                "                cycleFieldsContainer.appendChild(cycleSection);\n" +
                "            }\n" +
                "\n" +
                "            document.getElementById('graphContainer').style.display = 'none'; // Hide graph container initially\n" +
                "            cycleFormSubmitted = true; // Set flag to true after form submission\n" +
                "        });\n" +
                "\n" +
                "        document.getElementById('confirmData').addEventListener('click', function() {\n" +
                "            if (cycleFormSubmitted) {\n" +
                "                // If form is submitted and user confirms\n" +
                "                const form = document.getElementById('cycleForm');\n" +
                "                const formData = new FormData(form);\n" +
                "                const entries = Array.from(formData.entries());\n" +
                "\n" +
                "                // Simulating submission to \"/app/v1/battery/velocity\"\n" +
                "                console.log(\"Submitting data to /app/v1/battery/velocity:\");\n" +
                "                console.log(entries);\n" +
                "                \n" +
                "                alert('Form submitted successfully to /app/v1/battery/velocity');\n" +
                "                cycleFormSubmitted = false; // Reset form submission status\n" +
                "\n" +
                "                // Optionally clear or reset form fields and UI\n" +
                "                document.getElementById('cycleFieldsContainer').innerHTML = '';\n" +
                "                document.getElementById('cycles').value = '';\n" +
                "                document.getElementById('graphContainer').style.display = 'none';\n" +
                "            }\n" +
                "        });\n" +
                "\n" +
                "        function updateGraph() {\n" +
                "            const numberOfCycles = parseInt(document.getElementById('cycles').value, 10);\n" +
                "            const cRateData = [];\n" +
                "            const finalChargeData = [];\n" +
                "\n" +
                "            for (let i = 1; i <= numberOfCycles; i++) {\n" +
                "                const cRateInput = document.querySelector(`input[name=\"cRate_${i}\"]`);\n" +
                "                const finalChargeInput = document.querySelector(`input[name=\"finalCharge_${i}\"]`);\n" +
                "\n" +
                "                cRateData.push(parseFloat(cRateInput.value));\n" +
                "                finalChargeData.push(parseFloat(finalChargeInput.value));\n" +
                "            }\n" +
                "\n" +
                "            const ctx = document.getElementById('graph').getContext('2d');\n" +
                "            if (window.myLineChart) {\n" +
                "                window.myLineChart.destroy(); // Destroy existing chart instance\n" +
                "            }\n" +
                "\n" +
                "            window.myLineChart = new Chart(ctx, {\n" +
                "                type: 'line',\n" +
                "                data: {\n" +
                "                    labels: Array.from(Array(numberOfCycles), (_, i) => `Cycle ${i + 1}`),\n" +
                "                    datasets: [{\n" +
                "                        label: 'cRate vs Final Charge',\n" +
                "                        data: finalChargeData,\n" +
                "                        borderColor: 'rgba(75, 192, 192, 1)',\n" +
                "                        backgroundColor: 'rgba(75, 192, 192, 0.2)',\n" +
                "                        yAxisID: 'y-axis-1',\n" +
                "                    }, {\n" +
                "                        label: 'cRate',\n" +
                "                        data: cRateData,\n" +
                "                        borderColor: 'rgba(255, 159, 64, 1)',\n" +
                "                        backgroundColor: 'rgba(255, 159, 64, 0.2)',\n" +
                "                        yAxisID: 'y-axis-2',\n" +
                "                    }]\n" +
                "                },\n" +
                "                options: {\n" +
                "                    scales: {\n" +
                "                        yAxes: [{\n" +
                "                            type: 'linear',\n" +
                "                            display: true,\n" +
                "                            position: 'left',\n" +
                "                            id: 'y-axis-1',\n" +
                "                        }, {\n" +
                "                            type: 'linear',\n" +
                "                            display: true,\n" +
                "                            position: 'right',\n" +
                "                            id: 'y-axis-2',\n" +
                "                            gridLines: {\n" +
                "                                drawOnChartArea: false,\n" +
                "                            },\n" +
                "                        }],\n" +
                "                    }\n" +
                "                }\n" +
                "            });\n" +
                "\n" +
                "            document.getElementById('graphContainer').style.display = 'block';\n" +
                "        }\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>\n";
    }




}


