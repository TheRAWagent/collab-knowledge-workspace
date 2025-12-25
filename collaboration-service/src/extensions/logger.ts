import { Logger } from '@hocuspocus/extension-logger';

export const getLoggerExtension = () => {
  return new Logger();
};
