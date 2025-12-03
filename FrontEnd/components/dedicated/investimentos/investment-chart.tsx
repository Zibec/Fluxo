"use client";

import {
	LineChart,
	Line,
	XAxis,
	YAxis,
	CartesianGrid,
	Tooltip,
	ResponsiveContainer,
} from "recharts";
import { Historico } from "./investment-page";

interface InvestmentChartProps {
	data: Historico[];
}

export function InvestmentChart({ data }: InvestmentChartProps) {
	return (
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
					dataKey="valorAtualizado"
					stroke="oklch(0.205 0 0)"
					strokeWidth={3}
					dot={{ fill: "oklch(0.205 0 0)", r: 5 }}
					activeDot={{ r: 7 }}
				/>
			</LineChart>
		</ResponsiveContainer>
	);
}
