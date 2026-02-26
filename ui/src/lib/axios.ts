import Axios, { type AxiosRequestConfig, type AxiosError } from 'axios';

export const AXIOS_INSTANCE = Axios.create();

// Add a second `options` argument to pass extra options to each query
export const customInstance = <T>(
  config: AxiosRequestConfig,
  options?: AxiosRequestConfig,
): Promise<T> => {
  const promise = AXIOS_INSTANCE({
    ...config,
    ...options,
  }).then(({ data }) => data);
  return promise;
};
// Override the return error type for react-query and swr
export type ErrorType<Error> = AxiosError<Error>;
// Standard body type
export type BodyType<BodyData> = BodyData;
// Or wrap the body type if processing data before sending
// export type BodyType<BodyData> = CamelCase<BodyData>;