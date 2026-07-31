import { useEffect, useState } from "react";
import Results from "../components/Results";
import styles from "./Home.module.scss";
import axios from "axios";
import { useSearchParams } from "react-router-dom";
import ReactMarkdown from 'react-markdown';
import { exceptions } from "../utils/utils";

interface GoogleAPI {
  data: { organic_results: [{ link: string }] };
}

interface HibersafeAPI {
  data: { topSimilarity: [{ url: string }] };
}

export default function Home() {
  const [searchParams] = useSearchParams();

  const estrategia = searchParams.get("estrategia");
  const id = searchParams.get("id");
  const rag = searchParams.get("rag");
  const gpt = searchParams.get("gpt");
  const list = searchParams.get("list");
  const justSo = searchParams.get("just_so");
  const linkCount = searchParams.get("link_count") ?? 15;
  const minSimilarity = searchParams.get("min_similarity") ?? 0.75;

  let activeMode: 'LIST' | 'JUST_SO' | 'RAG' | 'GPT' | 'A' | 'B' | null = null;
  if (list) activeMode = 'LIST';
  else if (justSo) activeMode = 'JUST_SO';
  else if (rag) activeMode = 'RAG';
  else if (gpt) activeMode = 'GPT';
  else if (estrategia === "A" && id) activeMode = 'A';
  else if (estrategia === "B" && id) activeMode = 'B';

  const [exception, setException] = useState<string>(exceptions[0]);
  const [resultsA, setResultsA] = useState<string[]>([]);
  const [resultsB, setResultsB] = useState<string[]>([]);
  const [resultsRAG, setResultsRAG] = useState<string>('');
  const [resultsListRAG, setResultsListRAG] = useState<string[]>([]);
  const [stacktrace, setStacktrace] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [toLog, setToLog] = useState<boolean>(false);

  const calculateResultsA = async () => {
    setResultsA([]);
    setLoading(true);
    try {
      let returnInfoA = await axios.get<any, GoogleAPI>(
          `https://api.scaleserp.com/search?api_key=${process.env.REACT_APP_SCALESERP_GOOGLE_API_KEY}&q=site:stackoverflow.com ${stacktrace}&flatten_results=true`
      );
      if (returnInfoA.data.organic_results) {
        setResultsA(returnInfoA.data.organic_results.map(or => or.link));
      }
      setToLog(true);
    } catch (e) {
      alert("Ocorreu algum erro. Por favor, tente novamente.");
      console.log(e);
    } finally {
      setLoading(false);
    }
  };

  const calculateResultsB = async () => {
    setResultsB([]);
    setLoading(true);
    try {
      let returnInfoB = await axios.post<any, HibersafeAPI>(
          `/api/question/exceptionEnum/${exception}`,
          { stacktrace }
      );
      setResultsB(returnInfoB.data.topSimilarity.map(ts => ts.url));
      setToLog(true);
    } catch (e) {
      alert("Ocorreu algum erro. Por favor, tente novamente.");
      console.log(e);
    } finally {
      setLoading(false);
    }
  };

  const calculateRAGResult = async (useRAG: boolean, isList: boolean, useJustSo: boolean) => {
    setResultsRAG('');
    setResultsListRAG([]);
    setLoading(true);
    try {
      const endpoint = isList ? "list" : "prompt";
      let queryParams = `link_count=${linkCount}&min_similarity=${minSimilarity}`;

      if (!isList) {
        queryParams += `&rag=${useRAG}&just_so=${useJustSo}&limitDate=`;
      }

      let response = await axios.post<any, any>(
          `/api/rag/${endpoint}?${queryParams}`,
          stacktrace,
          { headers: { 'Content-Type': 'text/plain' } }
      );

      if (isList) {
        const linksArray = response.data.split('\n').filter((link: string) => link.trim() !== '');
        setResultsListRAG(linksArray);
      } else {
        setResultsRAG(response?.data);
      }
    } catch (e) {
      alert("Ocorreu algum erro. Por favor, tente novamente.");
      console.log(e);
    } finally {
      setLoading(false);
    }
  }

  const handleSearch = () => {
    if (!stacktrace) {
      alert("Informe uma stacktrace para continuar!");
      return;
    }
    switch (activeMode) {
      case 'A': calculateResultsA(); break;
      case 'B': calculateResultsB(); break;
      case 'LIST': calculateRAGResult(true, true, false); break;
      case 'JUST_SO': calculateRAGResult(false, false, true); break;
      case 'RAG': calculateRAGResult(true, false, false); break;
      case 'GPT': calculateRAGResult(false, false, false); break;
    }
  }

  useEffect(() => {
    if (toLog && (activeMode === 'A' || activeMode === 'B')) {
      axios.post<any, any>(`/api/log/`, {
        estrategia: activeMode,
        id,
        dados: activeMode === 'A' ? resultsA : resultsB,
        stacktrace,
        exception,
      });
      setToLog(false);
    }
  }, [activeMode, exception, id, resultsA, resultsB, stacktrace, toLog]);

  const renderInputForm = () => {
    if (!activeMode) {
      return <p>Nenhum modo selecionado. Por favor, insira parâmetros válidos na URL.</p>;
    }

    const needsException = activeMode === 'A' || activeMode === 'B';

    return (
        <div className={styles.inputGroup}>
          {needsException && (
              <>
                <label>Selecione a exceção lançada:</label>
                <select onChange={(e) => setException(e.target.value)} disabled={loading}>
                  {exceptions.map((exc, index) => (
                      <option key={index} value={exc}>{exc}</option>
                  ))}
                </select>
              </>
          )}
          <label>Informe a stacktrace:</label>
          <textarea
              id="stacktrace"
              onChange={(e) => setStacktrace(e.target.value)}
              maxLength={1200}
              disabled={loading}
          />
          <button onClick={handleSearch} disabled={!stacktrace || loading}>
            Buscar
          </button>
        </div>
    );
  }

  const renderResults = () => {
    if (!activeMode || loading) return null;
    const containerStyle = activeMode === 'B' ? styles.sideB : styles.sideA;

    return (
        <div className={styles.results}>
          <div className={containerStyle}>
            <h2>Resultados</h2>
            <div className={styles.resultList}>
              {activeMode === 'A' && (
                  resultsA.length > 0 ? resultsA.map((r, i) => <Results link={r} index={i} side={"A"} key={i} />) : <p>Nenhum resultado encontrado!</p>
              )}

              {activeMode === 'B' && (
                  resultsB.length > 0 ? resultsB.map((r, i) => <Results link={r} index={i} side={"B"} key={i} />) : <p>Nenhum resultado encontrado!</p>
              )}

              {activeMode === 'LIST' && (
                  resultsListRAG.length > 0 ? resultsListRAG.map((r, i) => <Results link={r} index={i} side={"RAG"} key={i} />) : <p>Nenhum resultado encontrado!</p>
              )}

              {(activeMode === 'RAG' || activeMode === 'GPT' || activeMode === 'JUST_SO') && (
                  resultsRAG ? <ReactMarkdown>{resultsRAG}</ReactMarkdown> : <p>Nenhum resultado encontrado!</p>
              )}
            </div>
          </div>
        </div>
    );
  }

  return (
      <div className={styles.pageRoot}>
        <h1>Hibersafe</h1>
        {renderInputForm()}
        {loading && (
            <img alt="Carregando..." height="100em" width="118em" src="loading.gif" />
        )}
        {renderResults()}
      </div>
  );
}
