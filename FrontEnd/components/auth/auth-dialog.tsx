"use client";

import type React from "react";
import { useState, useTransition } from "react";
import { useForm, SubmitHandler } from "react-hook-form";
import { LoginForm } from "../forms/login-form";
import { RegisterForm } from "../forms/register-form";

interface AuthDialogProps {
	isOpen: boolean;
	onClose: () => void;
}

type FormData = {
	name: string;
	email: string;
	password: string;
};

export function AuthDialog({ isOpen, onClose }: AuthDialogProps) {
	const [mode, setMode] = useState<"login" | "register">("login");
	const [isPending, startTransition] = useTransition();

	const {
		register,
		handleSubmit,
		watch,
		formState: { errors },
	} = useForm<FormData>();

	const onSubmit: SubmitHandler<FormData> = (data) => {
		startTransition(async () => {
			await new Promise((resolve) => setTimeout(resolve, 1500));
			console.log(data);
		});
	};

	if (!isOpen) return null;

	return (
		<div className="fixed inset-0 z-50 flex items-end md:items-center justify-center p-4">
			<div
				className="absolute inset-0 bg-black/50 backdrop-blur-sm"
				onClick={onClose}
			/>

			<div className="relative bg-white rounded-2xl md:rounded-2xl shadow-2xl w-full max-w-md max-h-[90vh] overflow-y-auto no-scrollbar">
				<div className="relative bg-linear-to-r from-blue-600 to-blue-700 px-4 md:px-6 py-6 md:py-8 text-white top-0 z-10">
					<button
						onClick={onClose}
						className="absolute top-4 right-4 p-2 hover:bg-white/20 rounded-lg transition"
						aria-label="Fechar diálogo"
					>
						<svg
							className="w-5 h-5"
							fill="none"
							stroke="currentColor"
							viewBox="0 0 24 24"
						>
							<path
								strokeLinecap="round"
								strokeLinejoin="round"
								strokeWidth={2}
								d="M6 18L18 6M6 6l12 12"
							/>
						</svg>
					</button>
					<h2 className="text-xl md:text-2xl font-bold mb-2 pr-8">
						{mode === "login" ? "Bem-vindo de volta" : "Crie sua conta"}
					</h2>
					<p className="text-blue-100 text-xs md:text-sm">
						{mode === "login"
							? "Acesse sua conta e gerencie suas finanças"
							: "Comece a controlar seu dinheiro hoje"}
					</p>
				</div>

				{mode === "register" && (
					<RegisterForm startTransition={startTransition} />
				)}

				{mode === "login" && <LoginForm startTransition={startTransition} />}

				<div className="px-4 md:px-6 py-4 bg-slate-50 border-t border-slate-200 text-center text-sm sticky bottom-0">
					<p className="text-slate-600 text-xs md:text-sm">
						{mode === "login" ? "Não tem conta? " : "Já tem conta? "}
						<button
							type="button"
							onClick={() => setMode(mode === "login" ? "register" : "login")}
							className="text-blue-600 hover:text-blue-700 font-semibold transition"
						>
							{mode === "login" ? "Criar conta" : "Fazer login"}
						</button>
					</p>
				</div>
			</div>
		</div>
	);
}
