import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { createDividaFormData } from "@/lib/service/dividas/divida-schema";
import Link from "next/link";

interface DebtSectionProps {
	dividas: createDividaFormData[];
}

export function DebtSection({ dividas }: DebtSectionProps) {
	return (
		<Card
			className="transition-colors"
			style={{
				backgroundColor: "var(--card)",
				color: "var(--card-foreground)",
				borderColor: "var(--border)",
			}}
		>
			<CardHeader>
				<Link href="/dashboard/dividas">
					<CardTitle
						className="text-xl font-semibold cursor-pointer transition-colors"
						style={{
							color: "var(--foreground)",
						}}
					>
						Dívidas
					</CardTitle>
				</Link>
			</CardHeader>

			<CardContent className="space-y-4">
				{dividas &&
					dividas.map((debt) => (
						<div key={debt.nome} className="space-y-1">
							<p
								className="text-sm font-medium"
								style={{ color: "var(--muted-foreground)" }}
							>
								{debt.nome}
							</p>
							<p
								className="text-sm"
								style={{ color: "var(--muted-foreground)" }}
							>
								Pago R$ {debt.valorAmortizado} de R$ {debt.valorDivida}
							</p>
						</div>
					))}
			</CardContent>
		</Card>
	);
}
