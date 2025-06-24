import { useEffect, useState } from "react";
import Results from "../components/Results";
import styles from "./Home.module.scss";
import axios from "axios";
import { useSearchParams } from "react-router-dom";

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

export default function Home() {
  const [searchParams] = useSearchParams();
  var estrategia = searchParams.get("estrategia");
  var id = searchParams.get("id");
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

  return (
    <div className={styles.pageRoot}>
      <h1>Hibersafe</h1>
      {estrategia && id ? (
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
      ) : (
        "URL Inválida!"
      )}
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
        </div>
      )}
    </div>
  );
}
