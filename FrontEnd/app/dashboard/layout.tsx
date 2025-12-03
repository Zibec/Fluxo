import type React from "react";
import { Header } from "@/components/layout/header";
import { DataProvider } from "@/hooks/data-context";

export default function DashboardLayout({
	children,
}: Readonly<{
	children: React.ReactNode;
}>) {
	return (
		<div>
			<DataProvider>
				<Header />
				{children}
			</DataProvider>
		</div>
	);
}
