/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/date/filters';
import DocService from '@/entities/doc/doc.service';
import { Doc } from '@/shared/model/doc.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Doc Service', () => {
    let service: DocService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new DocService();
      currentDate = new Date();
      elemDefault = new Doc(
        123,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        'AAAAAAA',
        currentDate,
        currentDate,
        currentDate,
        0,
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            purchaseDate: dayjs(currentDate).format(DATE_FORMAT),
            startReadingDate: dayjs(currentDate).format(DATE_FORMAT),
            endReadingDate: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Doc', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            purchaseDate: dayjs(currentDate).format(DATE_FORMAT),
            startReadingDate: dayjs(currentDate).format(DATE_FORMAT),
            endReadingDate: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            purchaseDate: currentDate,
            startReadingDate: currentDate,
            endReadingDate: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Doc', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Doc', async () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            subTitle: 'BBBBBB',
            publishYear: 'BBBBBB',
            coverImgPath: 'BBBBBB',
            editionNumer: 1,
            summary: 'BBBBBB',
            purchaseDate: dayjs(currentDate).format(DATE_FORMAT),
            startReadingDate: dayjs(currentDate).format(DATE_FORMAT),
            endReadingDate: dayjs(currentDate).format(DATE_FORMAT),
            price: 1,
            copies: 1,
            pageNumber: 1,
            numDoc: 'BBBBBB',
            myNotes: 'BBBBBB',
            keywords: 'BBBBBB',
            toc: 'BBBBBB',
            filename: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            purchaseDate: currentDate,
            startReadingDate: currentDate,
            endReadingDate: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Doc', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Doc', async () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            subTitle: 'BBBBBB',
            summary: 'BBBBBB',
            endReadingDate: dayjs(currentDate).format(DATE_FORMAT),
            copies: 1,
            pageNumber: 1,
            keywords: 'BBBBBB',
          },
          new Doc()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            purchaseDate: currentDate,
            startReadingDate: currentDate,
            endReadingDate: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Doc', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Doc', async () => {
        const returnedFromService = Object.assign(
          {
            title: 'BBBBBB',
            subTitle: 'BBBBBB',
            publishYear: 'BBBBBB',
            coverImgPath: 'BBBBBB',
            editionNumer: 1,
            summary: 'BBBBBB',
            purchaseDate: dayjs(currentDate).format(DATE_FORMAT),
            startReadingDate: dayjs(currentDate).format(DATE_FORMAT),
            endReadingDate: dayjs(currentDate).format(DATE_FORMAT),
            price: 1,
            copies: 1,
            pageNumber: 1,
            numDoc: 'BBBBBB',
            myNotes: 'BBBBBB',
            keywords: 'BBBBBB',
            toc: 'BBBBBB',
            filename: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            purchaseDate: currentDate,
            startReadingDate: currentDate,
            endReadingDate: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Doc', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Doc', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Doc', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
