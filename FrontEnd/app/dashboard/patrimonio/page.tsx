"use client";

import { relatorioService } from "@/lib/service/relatorio/relatorio-service";
import { useEffect, useState } from "react";
import {
	LineChart,
	ResponsiveContainer,
	CartesianGrid,
	XAxis,
	YAxis,
	Tooltip,
	Line,
} from "recharts";

interface DataEntrada {
	data: Date;
	valor: number;
}

export default function PerfisPage() {
	const [data, setData] = useState<DataEntrada[]>([]);

	useEffect(() => {
		const fetchHistorico = async () => {
			const response = await relatorioService.getPatrimonioHistorico();
			console.log(response);
			setData(response);
		};
		fetchHistorico();
	}, []);

	return (
		<div className="min-h-screen bg-[var(--color-background)] text-[var(--color-foreground)] transition-colors">
			<main className="max-w-4xl mx-auto px-6 py-8">
				{/* Cabeçalho */}
				<div className="flex items-center justify-between mb-6">
					<h1 className="text-3xl font-bold text-[var(--color-foreground)]">
						Historico de Patrimonio
					</h1>
				</div>
				<div className="h-[60vh] w-[100vh]">
					<ResponsiveContainer width="100%" height="100%">
						<LineChart
							data={data}
							margin={{ top: 20, right: 30, left: 0, bottom: 10 }}
						>
							<CartesianGrid strokeDasharray="4 4" stroke="oklch(0.92 0 0)" />

							<XAxis
								dataKey="data"
								stroke="oklch(0.56 0 0)"
								tick={{ fontSize: 12 }}
								tickMargin={8}
							/>

							<YAxis
								stroke="oklch(0.56 0 0)"
								tick={{ fontSize: 12 }}
								tickMargin={8}
							/>

							<Tooltip
								contentStyle={{
									backgroundColor: "oklch(1 0 0)",
									border: "1px solid oklch(0.92 0 0)",
									borderRadius: "12px",
								}}
								labelStyle={{ fontWeight: "600" }}
								itemStyle={{ fontSize: "0.875rem" }}
							/>

							<Line
								type="monotone"
								dataKey="valor"
								stroke="oklch(0.205 0 0)"
								strokeWidth={3}
								dot={{ fill: "oklch(0.205 0 0)", r: 5 }}
								activeDot={{ r: 7 }}
							/>
						</LineChart>
					</ResponsiveContainer>
				</div>
			</main>
		</div>
	);
}
