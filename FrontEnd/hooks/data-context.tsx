"use client";

import { createCategoriaFormData } from "@/lib/service/categoria/categoria-schemas";
import { categoriasService } from "@/lib/service/categoria/categoria-service";
import {
	createCartaoFormData,
	createContaFormData,
} from "@/lib/service/contas-cartoes/contas-cartoes-schemas";
import {
	cartoesService,
	contasService,
} from "@/lib/service/contas-cartoes/contas-cartoes-service";
import { createPerfilFormData } from "@/lib/service/perfil/perfil-schema";
import { perfilService } from "@/lib/service/perfil/perfil-service";
import {
	createContext,
	Dispatch,
	SetStateAction,
	useCallback,
	useEffect,
	useState,
} from "react";

interface dataTypes {
	categorias: createCategoriaFormData[] | null;
	setCategorias: Dispatch<SetStateAction<createCategoriaFormData[]>> | null;

	perfis: createPerfilFormData[] | null;
	setPerfis: Dispatch<SetStateAction<createPerfilFormData[]>> | null;

	contas: createContaFormData[] | null;
	setContas: Dispatch<SetStateAction<createContaFormData[]>> | null;

	cartoes: createCartaoFormData[] | null;
	setCartoes: Dispatch<SetStateAction<createCartaoFormData[]>> | null;
}

export const DataContext = createContext<any>(null);

export function DataProvider({ children }) {
	const [categorias, setCategorias] = useState<createCategoriaFormData[]>();
	const [perfis, setPerfis] = useState<createPerfilFormData[]>();
	const [contas, setContas] = useState<createContaFormData[]>();
	const [cartoes, setCartoes] = useState<createCartaoFormData[]>();

	const fetchCategories = useCallback(async () => {
		const categorias = await categoriasService.getAllCategorias();
		setCategorias(categorias);
	}, [categorias]);

	const fetchPerfis = useCallback(async () => {
		const perfis = await perfilService.getAllPerfis();
		setPerfis(perfis);
	}, [perfis]);

	const fetchContas = useCallback(async () => {
		const contas = await contasService.getAllContas();
		setContas(contas);
	}, [contas]);

	const fetchCartoes = useCallback(async () => {
		const cartoes = await cartoesService.getAllCartoes();
		setCartoes(cartoes);
	}, [cartoes]);

	useEffect(() => {
		fetchCategories();
		fetchPerfis();
		fetchContas();
		fetchCartoes();
	}, []);

	const values: dataTypes = {
		categorias: categorias,
		setCategorias: setCategorias,

		perfis: perfis,
		setPerfis: setPerfis,

		contas: contas,
		setContas: setContas,

		cartoes: cartoes,
		setCartoes: setCartoes,
	};

	return <DataContext.Provider value={values}>{children}</DataContext.Provider>;
}
