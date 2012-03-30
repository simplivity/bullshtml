	<tr>
		<td><a href='$it.normalizedPath$.html'>$it.path$</a></td>
		<td class='value'>$it.fileCount$</td>
		<td><table cellpadding='0px' cellspacing='0px' class='percentgraph'>
				<tr class='percentgraph'>
					<td align='right' class='percentgraph' width='40'>$it.functionCoverageString$%</td>
					<td class='percentgraph'>
						<div class='percentgraph'>
							<div $it.functionCoverageStyle$>
								<span class='text'>$it.coveredFunctionCount$/$it.functionCount$</span>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</td>
		<td>
			<table cellpadding='0px' cellspacing='0px' class='percentgraph'>
				<tr class='percentgraph'>
					<td align='right' class='percentgraph' width='40'>$it.branchCoverageString$%</td>
					<td class='percentgraph'>
						<div class='percentgraph'>
							<div $it.branchCoverageStyle$>
								<span class='text'>$it.coveredBranchCount$/$it.branchCount$</span>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>					 