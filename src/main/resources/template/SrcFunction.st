	<tr>
		<td><a href='#$it.line$'>$it.name$</a></td>
		<td>
			$if(it.covered)$
				Covered
			$else$
				Not Covered
			$endif$
		</td>
		<td>
			<table cellpadding='0px' cellspacing='0px' class='percentgraph'>
				<tr class='percentgraph'>
					<td align='right' class='percentgraph' width='40'>$it.branchCoverage$%</td>
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
