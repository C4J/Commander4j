/*jshint esversion: 6 */
/*jshint esversion: 8 */

	/**
 * @param {HTMLElement} htmlTable
 * @param {String} bom_id
 * @param {String} bom_version
 * @param {String} stage
 * @param {String} input_output
 */
	async function buildRequiredMaterialsTable(htmlTable,bom_id,bom_version,stage,input_output)
	{
	
		let payload = {
			action: "getMaterialsForBOM",
			bom_id: bom_id,
			bom_version: bom_version,
			stage:stage,
			input_output:input_output
		};
	
		console.log(JSON.stringify(payload));
	
		let response = await fetch(getContextPath()+"/ViewBOM", { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) })
			.then(function(response) { return response.json(); })
			.then(function(boms) {
				let out = "";
				for (let xxx of boms) {
					out += `<tr>
					<td style="color:black; background-color:#f0f8ff; width:20%">`+xxx.material+`</td>
				    <td style="color:black; background-color:#f0f8ff; width:60%; text-align:left">`+xxx.description+`</td>
				    <td style="color:black; background-color:#f0f8ff; width:20%">`+xxx.location_id+`</td>
					</tr>`;
				}

				htmlTable.innerHTML = out;
			})
	
		console.log(response);
	
	}
	