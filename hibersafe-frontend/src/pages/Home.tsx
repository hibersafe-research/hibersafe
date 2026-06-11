import { useEffect, useState } from "react";
import Results from "../components/Results";
import styles from "./Home.module.scss";
import axios from "axios";
import { useSearchParams } from "react-router-dom";
import ReactMarkdown from 'react-markdown';

interface GoogleAPI {
  data: {
    organic_results: [{ link: string }];
  };
}

interface HibersafeAPI {
  data: {
    topSimilarity: [{ url: string }];
  };
}

interface RagAPI {
  data: string
}

export default function Home() {
  const [searchParams] = useSearchParams();
  let estrategia = searchParams.get("estrategia");
  let id = searchParams.get("id");
  let rag = searchParams.get("rag");
  let gpt = searchParams.get("gpt");
  let linkCount = searchParams.get("link_count") ?? 15
  let minSimilarity = searchParams.get("min_similarity") ?? 0.75

  const exceptions = [
    "AnnotationException",
    "AssertionFailure",
    "AuthException",
    "CallbackException",
    "ConstraintViolationException",
    "DataException",
    "DuplicateMappingException",
    "EntityFilterException",
    "FetchNotFoundException",
    "GenericJDBCException",
    "HibernateError",
    "HibernateException",
    "InstantiationException",
    "InvalidMappingException",
    "JDBCConectionException",
    "JDBCException",
    "LazyInitializationException",
    "LockAcquisitionException",
    "LockTimeoutException",
    "MappingException",
    "NonUniqueObjectException",
    "NonUniqueResultException",
    "ObjectDeletedException",
    "ObjectNotFoundException",
    "PersistentObjectException",
    "PessimisticLockException",
    "PropertyAccessException",
    "PropertyNotFoundException",
    "PropertySetterAccessException",
    "PropertyValueException",
    "QueryException",
    "QueryParameterException",
    "QueryTimeoutException",
    "ResourceClosedException",
    "SessionException",
    "SnapshotIsolationException",
    "SQLGrammarException",
    "StaleObjectStateException",
    "StaleStateException",
    "TransactionException",
    "TransactionManagementException",
    "TransactionSerializationException",
    "TransientObjectException",
    "TransientPropertyValueException",
    "TypeMismatchException",
    "UnknownEntityTypeException",
    "UnknownFilterException",
    "UnknownProfileException",
    "UnresolvableObjectException",
    "UnsupportedLockAttemptException",
    "WrongClassException",
  ];
  const [exception, setException] = useState<string>(exceptions[0]);
  const [resultsA, setResultsA] = useState<string[]>([]);
  const [resultsB, setResultsB] = useState<string[]>([]);
  const [resultsRAG, setResultsRAG] = useState<string>('');
  const [stacktrace, setStacktrace] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [toLog, setToLog] = useState<boolean>(false);

  const calculateResultsA = async () => {
    setResultsA([]);
    if (!stacktrace) {
      alert("Informe uma stacktrace para continuar!");
    } else {
      setLoading(true);
      try {
        let returnInfoA = await axios.get<any, GoogleAPI>(
          `https://api.scaleserp.com/search?api_key=${process.env.REACT_APP_SCALESERP_GOOGLE_API_KEY}&q=site:stackoverflow.com ${stacktrace}&flatten_results=true`
        );

        if (returnInfoA.data.organic_results) {
          returnInfoA.data.organic_results.forEach(async (or) => {
            setResultsA((arr) => [...arr, or.link]);
          });
        }

        setToLog(true);

        setLoading(false);
      } catch (e) {
        alert("Ocorreu algum erro. Por favor, tente novamente.");
        setResultsA([]);
        setLoading(false);
        console.log(e);
      }
    }
  };

  const calculateResultsB = async () => {
    setResultsB([]);
    if (!stacktrace) {
      alert("Informe uma stacktrace para continuar!");
    } else {
      setLoading(true);
      try {
        let returnInfoB = await axios.post<any, HibersafeAPI>(
          `http://localhost:8080/api/question/exceptionEnum/${exception}`,
          { stacktrace }
        );
        returnInfoB.data.topSimilarity.forEach(async (ts) => {
          setResultsB((arr) => [...arr, ts.url]);
        });

        setToLog(true);

        setLoading(false);
      } catch (e) {
        alert("Ocorreu algum erro. Por favor, tente novamente.");
        setResultsB([]);
        setLoading(false);
        console.log(e);
      }
    }
  };

  const calculateRAGResult = async (useRAG: boolean) => {
    setResultsRAG('');
    if (!stacktrace) {
      alert("Informe uma stacktrace para continuar!");
    } else {
      setLoading(true);
      try {
        let returnRAG = await axios.post<any, RagAPI>(
          `http://localhost:8080/api/rag?link_count=${linkCount}&min_similarity=${minSimilarity}&rag=${useRAG}`,
          { stacktrace }
        );
        setResultsRAG(returnRAG?.data);
        setLoading(false);
      } catch (e) {
        alert("Ocorreu algum erro. Por favor, tente novamente.");
        setResultsB([]);
        setLoading(false);
        console.log(e);
      }
    }
  }

  const calculateRAG = async () => {await calculateRAGResult(true)}

  const calculateGPT  = async () => {await calculateRAGResult(false)}

  useEffect(() => {
    if (toLog && estrategia === "A") {
      axios.post<any, any>(`http://localhost:8080/api/log/`, {
        estrategia,
        id,
        dados: resultsA,
        stacktrace,
        exception,
      });
    }
    setToLog(false);
  }, [estrategia, exception, id, resultsA, stacktrace, toLog]);

  useEffect(() => {
    if (toLog && estrategia === "B") {
      axios.post<any, any>(`http://localhost:8080/api/log/`, {
        estrategia,
        id,
        dados: resultsB,
        stacktrace,
        exception,
      });
    }
    setToLog(false);
  }, [estrategia, exception, id, resultsB, stacktrace, toLog]);

  const renderOldStrategies = () => {
    return estrategia && id && (
      <div className={styles.inputGroup}>
        <label>Selecione a exceção lançada e informe a stacktrace:</label>
        <select
          onChange={(e) => setException(e.target.value)}
          disabled={loading}
        >
          {exceptions.map((exception) => (
            <option>{exception}</option>
          ))}
        </select>
        <textarea
          id="stacktrace"
          onChange={(e) => setStacktrace(e.target.value)}
          maxLength={1200}
          disabled={loading}
        />
        <button
          onClick={
            estrategia === "A"
              ? calculateResultsA
              : estrategia === "B"
                ? calculateResultsB
                : undefined
          }
          disabled={!stacktrace || loading}
        >
          Buscar
        </button>
      </div>
    )
  }

  const renderRAG = () => {
    return (rag || gpt) && (
      <div className={styles.inputGroup}>
        <label>Informe a stacktrace:</label>
        <textarea
          id="stacktrace"
          onChange={(e) => setStacktrace(e.target.value)}
          maxLength={1200}
          disabled={loading}
        />
        <button
          onClick={rag ? calculateRAG : calculateGPT}
          disabled={loading}
        >
          Buscar
        </button>
      </div>
    )
  }

  return (
    <div className={styles.pageRoot}>
      <h1>Hibersafe</h1>
      {renderOldStrategies()}
      {renderRAG()}
      {loading && (
        <img
          alt="Carregando..."
          height="100em"
          width="118em%"
          src="loading.gif"
        />
      )}
      {!loading && (
        <div className={styles.results}>
          {estrategia === "A" && (
            <div className={styles.sideA}>
              <h2>Resultados</h2>
              <div className={styles.resultList}>
                {resultsA.length > 0 ? (
                  resultsA.map((r, index) => (
                    <Results link={r} index={index} side={"A"} key={index} />
                  ))
                ) : (
                  <p>Nenhum resultado encontrado!</p>
                )}
              </div>
            </div>
          )}
          {estrategia === "B" && (
            <div className={styles.sideB}>
              <h2>Resultados</h2>
              <div className={styles.resultList}>
                {resultsB.length > 0 ? (
                  resultsB.map((r, index) => (
                    <Results link={r} index={index} side={"B"} key={index} />
                  ))
                ) : (
                  <p>Nenhum resultado encontrado!</p>
                )}
              </div>
            </div>
          )}
          {(rag || gpt) && (
            <div className={styles.sideA}>
              <h2>Resultados</h2>
              <div className={styles.resultList}>
                {resultsRAG ? (
                  <div className={styles.resultList}>
                    <ReactMarkdown>
                      {resultsRAG}
                    </ReactMarkdown>
                  </div>
                ) : (
                  <p>Nenhum resultado encontrado!</p>
                )}
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
